package cn.tedu.fitnessClub.filter;

import cn.tedu.fitnessClub.restful.JsonResult;
import cn.tedu.fitnessClub.restful.ServiceCode;
import cn.tedu.fitnessClub.security.LoginPrincipal;
import com.alibaba.fastjson.JSON;
import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

/**
 * <p>JWT过滤器</p>
 *
 * <p>此过滤器的主要作用</p>
 * <ul>
 *     <li>接收客户端提交的请求中的JWT</li>
 *     <li>尝试解析客户端提交的请求中的有效JWT</li>
 *     <li>将解析成功得到的数据创建为Authentication对象，并存入到SecurityContext中</li>
 * </ul>
 */
@Slf4j
@Component
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    @Value("${fitness.jwt.secret-key}")
    private String secretKey;

    /**
     * JWT的最小长度值
     */
    public static final int JWT_MIN_LENGTH = 113;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        log.debug("JWT过滤器开始执行……");

        // 清空SecurityContext，避免【此前携带JWT成功访问后，在接下来的一段时间内不携带JWT也能访问】
        // SecurityContextHolder.clearContext();

        // 根据业内惯用的做法，客户端提交的请求中的JWT应该存放于请求头（Request Header）中的名为Authorization属性中
        String jwt = request.getHeader("Authorization");
        log.debug("客户端携带的JWT：{}", jwt);

        // 判断客户端是否携带了有效的JWT
        if (!StringUtils.hasText(jwt) || jwt.length() < JWT_MIN_LENGTH) {
            // 如果JWT无效，直接放行
            log.debug("客户端没有携带有效的JWT，将放行，由后续的过滤器等组件继续处理此请求……");
            filterChain.doFilter(request, response);
            return;
        }

        // 尝试解析JWT，并处理可能出现的异常
        Claims claims = null;
        response.setContentType("application/json; charset=utf-8");
        try {
            claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(jwt).getBody();
        } catch (MalformedJwtException e) {
            String message = "非法访问！";
            log.warn("解析JWT时出现MalformedJwtException，将响应错误消息：{}", message);
            PrintWriter writer = response.getWriter();
            JsonResult<Void> jsonResult = JsonResult.fail(ServiceCode.ERROR_JWT_MALFORMED, message);
            String jsonString = JSON.toJSONString(jsonResult);
            writer.println(jsonString);
            writer.close();
            return;
        } catch (SignatureException e) {
            String message = "非法访问！";
            log.warn("解析JWT时出现SignatureException，将响应错误消息：{}", message);
            PrintWriter writer = response.getWriter();
            JsonResult<Void> jsonResult = JsonResult.fail(ServiceCode.ERROR_JWT_SIGNATURE, message);
            String jsonString = JSON.toJSONString(jsonResult);
            writer.println(jsonString);
            writer.close();
            return;
        } catch (ExpiredJwtException e) {
            String message = "您的登录信息已过期，请重新登录！";
            log.warn("解析JWT时出现ExpiredJwtException，将响应错误消息：{}", message);
            PrintWriter writer = response.getWriter();
            JsonResult<Void> jsonResult = JsonResult.fail(ServiceCode.ERROR_JWT_EXPIRED, message);
            String jsonString = JSON.toJSONString(jsonResult);
            writer.println(jsonString);
            writer.close();
            return;
        } catch (Throwable e) {
            String message = "服务器忙，请稍后再次尝试！（开发过程中，如果看到此提示，请检查控制台的信息，并补充处理异常的方法）";
            log.warn("解析JWT时出现Throwable，将响应错误消息：{}", message);
            log.warn("异常类型：{}", e.getClass());
            log.warn("异常信息：{}", e.getMessage());
            e.printStackTrace(); // 打印异常的跟踪信息，主要是为了在开发阶段更好的检查出现异常的原因
            PrintWriter writer = response.getWriter();
            JsonResult<Void> jsonResult = JsonResult.fail(ServiceCode.ERROR_UNKNOWN, message);
            String jsonString = JSON.toJSONString(jsonResult);
            writer.println(jsonString);
            writer.close();
            return;
        }

        // 从解析JWT得到的Claims中获取此前存入到JWT中的数据
        Long id = claims.get("id", Long.class);
        String username = claims.get("username", String.class);
        String authoritiesJsonString = claims.get("authoritiesJsonString", String.class);
        log.debug("解析JWT结束，id={}，username={}", id, username);

        // 创建当事人对象，用于存入到Authentication对象中
        LoginPrincipal loginPrincipal = new LoginPrincipal();
        loginPrincipal.setId(id);
        loginPrincipal.setUsername(username);

        // 临时处理认证信息中的权限
        List<SimpleGrantedAuthority> authorities = JSON.parseArray(authoritiesJsonString, SimpleGrantedAuthority.class);
        log.debug("将JWT中的权限列表的JSON字符串解析为List<GrantedAuthority>类型，结果：{}", authorities);

        // 创建Authentication对象
        Object principal = loginPrincipal;
        Object credentials = null;
        Authentication authentication = new UsernamePasswordAuthenticationToken(principal, credentials, authorities);
        log.debug("即将存入到SecurityContext中的Authentication对象：{}", authentication);

        // 将Authentication对象存入到SecurityContext中
        SecurityContext securityContext = SecurityContextHolder.getContext();
        securityContext.setAuthentication(authentication);
        log.debug("已经将Authentication对象存入到SecurityContext");

        // 放行
        log.debug("JWT过滤器执行完毕，将放行");
        filterChain.doFilter(request, response);
    }

}
