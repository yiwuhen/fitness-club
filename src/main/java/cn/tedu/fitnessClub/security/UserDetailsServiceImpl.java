package cn.tedu.fitnessClub.security;

import cn.tedu.fitnessClub.mapper.AdminMapper;
import cn.tedu.fitnessClub.pojo.vo.AdminLoginInfoVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private AdminMapper adminMapper;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        log.debug("Spring Security调用了loadUserByUsername()方法，参数：{}", s);

        AdminLoginInfoVO loginInfo = adminMapper.getLoginInfoByUsername(s);
        log.debug("从数据库中根据用户名【{}】查询登录信息，结果：{}", s, loginInfo);

        if (loginInfo == null) {
            return null;
        }

        List<GrantedAuthority> authorities = new ArrayList<>();
        List<String> permissions = loginInfo.getPermissions();
        for (String permission : permissions) {
            authorities.add(new SimpleGrantedAuthority(permission));
        }

        AdminDetails adminDetails = new AdminDetails(
                loginInfo.getId(),
                loginInfo.getUsername(),
                loginInfo.getPassword(),
                loginInfo.getEnable() == 1,
                authorities);
        log.debug("即将向Spring Security返回UserDetails类型的对象：{}", adminDetails);
        return adminDetails;
    }

}
