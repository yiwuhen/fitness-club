package cn.tedu.fitnessClub.service.impl;

import cn.tedu.fitnessClub.ex.ServiceException;
import cn.tedu.fitnessClub.mapper.AdminMapper;
import cn.tedu.fitnessClub.mapper.AdminRoleMapper;
import cn.tedu.fitnessClub.pojo.dto.AdminAddNewDTO;
import cn.tedu.fitnessClub.pojo.dto.AdminLoginInfoDTO;
import cn.tedu.fitnessClub.pojo.entity.Admin;
import cn.tedu.fitnessClub.pojo.entity.AdminRole;
import cn.tedu.fitnessClub.pojo.vo.AdminListItemVO;
import cn.tedu.fitnessClub.restful.ServiceCode;
import cn.tedu.fitnessClub.security.AdminDetails;
import cn.tedu.fitnessClub.service.IAdminService;
import com.alibaba.fastjson.JSON;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
public class AdminServiceImpl implements IAdminService {

    @Value("${fitness.jwt.secret-key}")
    private String secretKey;
    @Value("${fitness.jwt.duration-in-minute}")
    private Long durationInMinute;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AdminMapper adminMapper;
    @Autowired
    private AdminRoleMapper adminRoleMapper;
    @Autowired
    private AuthenticationManager authenticationManager;

    public AdminServiceImpl() {
        log.debug("创建业务类对象：AdminServiceImpl");
    }

    @Override
    public String login(AdminLoginInfoDTO adminLoginInfoDTO) {
        log.debug("开始处理【管理员登录】的业务，参数：{}", adminLoginInfoDTO);
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                adminLoginInfoDTO.getUsername(), adminLoginInfoDTO.getPassword());
        Authentication authenticateResult
                = authenticationManager.authenticate(authentication);
        log.debug("认证通过！（如果未通过，过程中将抛出异常，你不会看到此条日志！）");
        log.debug("认证结果：{}", authenticateResult);
        log.debug("认证结果中的当事人：{}", authenticateResult.getPrincipal());

        // 使用JWT机制时，登录成功后不再需要将认证信息存入到SecurityContext
        // SecurityContext securityContext = SecurityContextHolder.getContext();
        // securityContext.setAuthentication(authenticateResult);

        // 需要存入到JWT中的数据
        AdminDetails adminDetails = (AdminDetails) authenticateResult.getPrincipal();
        Map<String, Object> claims = new HashMap<>();
        claims.put("id", adminDetails.getId());
        claims.put("username", adminDetails.getUsername());
        String authoritiesJsonString = JSON.toJSONString(adminDetails.getAuthorities());
        claims.put("authoritiesJsonString", authoritiesJsonString);

        // 生成JWT，以下代码是相对固定的
        String jwt = Jwts.builder()
                // Header：声明算法与Token类型
                .setHeaderParam("alg", "HS256")
                .setHeaderParam("typ", "JWT")
                // Payload：数据，具体表现为Claims
                .setClaims(claims)
                .setExpiration(new Date(System.currentTimeMillis() + durationInMinute * 60 * 1000))
                // Verify Signature：验证签名
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
        log.debug("生成了JWT数据：{}", jwt);
        return jwt;
    }

    @Override
    public void addNew(AdminAddNewDTO adminAddNewDTO) {
        // 检查角色ID的数组中是否包含1号角色，如果包含，则抛出异常
        Long[] roleIds = adminAddNewDTO.getRoleIds();
        for (Long roleId : roleIds) {
            if (roleId == 1) {
                // 是：抛出异常
                String message = "添加管理员失败，非法访问！";
                log.warn(message);
                throw new ServiceException(ServiceCode.ERROR_CONFLICT, message);
            }
        }

        // 检查用户名是否被占用
        {
            // 从参数对象中获取管理员的用户名
            String username = adminAddNewDTO.getUsername();
            // 调用Mapper对象的countByUsername()执行统计
            int count = adminMapper.countByUsername(username);
            // 判断统计结果是否大于0
            if (count > 0) {
                // 是：抛出异常
                String message = "添加管理员失败，用户名已经被占用！";
                log.warn(message);
                throw new ServiceException(ServiceCode.ERROR_CONFLICT, message);
            }
        }

        // 检查手机号码是否被占用
        {
            String phone = adminAddNewDTO.getPhone();
            int count = adminMapper.countByPhone(phone);
            if (count > 0) {
                String message = "添加管理员失败，手机号码已经被占用！";
                log.warn(message);
                throw new ServiceException(ServiceCode.ERROR_CONFLICT, message);
            }
        }

        // 检查电子邮箱是否被占用
        {
            String email = adminAddNewDTO.getEmail();
            int count = adminMapper.countByEmail(email);
            if (count > 0) {
                String message = "添加管理员失败，电子邮箱已经被占用！";
                log.warn(message);
                throw new ServiceException(ServiceCode.ERROR_CONFLICT, message);
            }
        }

        // 创建Admin对象
        Admin admin = new Admin();
        // 复制属性
        BeanUtils.copyProperties(adminAddNewDTO, admin);
        // 补全Admin对象的属性值：lastLoginIP(null) / loginCount(0) / gmtLastLogin(null)
        admin.setLoginCount(0);
        // 从Admin对象中取出原密码，执行加密，再将密文存入到Admin对象中
        String rawPassword = admin.getPassword();
        String encodedPassword = passwordEncoder.encode(rawPassword);
        admin.setPassword(encodedPassword);
        // 调用Mapper对象的insert()执行插入
        int rows = adminMapper.insert(admin);
        if (rows != 1) {
            String message = "添加管理员失败，服务器忙，请稍后再尝试！";
            log.warn(message);
            throw new ServiceException(ServiceCode.ERROR_INSERT, message);
        }

        // 为新添加的管理员分配角色
        // 基于角色ID的数组来创建List<AdminRole>
        List<AdminRole> adminRoleList = new ArrayList<>();
        for (Long roleId : roleIds) {
            AdminRole adminRole = new AdminRole();
            adminRole.setAdminId(admin.getId());
            adminRole.setRoleId(roleId);
            adminRoleList.add(adminRole);
        }
        // 调用AdminRoleMapper对象的insertBatch()执行插入
        rows = adminRoleMapper.insertBatch(adminRoleList);
        if (rows < 1) {
            String message = "添加管理员失败，服务器忙，请稍后再尝试！";
            log.warn(message);
            throw new ServiceException(ServiceCode.ERROR_INSERT, message);
        }
    }

    @Override
    public void delete(Long id) {
        log.debug("开始处理【根据id删除管理员】的业务，参数：{}", id);
        // 检查尝试删除的是否为1号管理员
        if (id == 1) {
            String message = "删除管理员失败，尝试访问的数据不存在！";
            log.warn(message);
            throw new ServiceException(ServiceCode.ERROR_NOT_FOUND, message);
        }

        // 检查尝试删除的数据是否存在
        Object queryResult = adminMapper.getStandardById(id);
        if (queryResult == null) {
            String message = "删除管理员失败，尝试访问的数据不存在！";
            log.warn(message);
            throw new ServiceException(ServiceCode.ERROR_NOT_FOUND, message);
        }

        // 执行删除--管理员表
        log.debug("即将执行删除数据，参数：{}", id);
        int rows = adminMapper.deleteById(id);
        if (rows != 1) {
            String message = "删除管理员失败，服务器忙，请稍后再尝试！";
            log.warn(message);
            throw new ServiceException(ServiceCode.ERROR_DELETE, message);
        }

        // 执行删除--管理员与角色的关联表
        log.debug("即将执行删除关联数据，参数：{}", id);
        rows = adminRoleMapper.deleteByAdminId(id);
        if (rows < 1) {
            String message = "删除管理员失败，服务器忙，请稍后再尝试！";
            log.warn(message);
            throw new ServiceException(ServiceCode.ERROR_DELETE, message);
        }
    }

    @Override
    public List<AdminListItemVO> list() {
        log.debug("开始处理【查询管理员列表】的业务，参数：无");
        List<AdminListItemVO> list = adminMapper.list();
        Iterator<AdminListItemVO> iterator = list.iterator();
        while (iterator.hasNext()) {
            AdminListItemVO item = iterator.next();
            if (item.getId() == 1) {
                iterator.remove();
                break;
            }
        }
        return list;
    }

}
