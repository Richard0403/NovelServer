package cc.ifinder.novel.security.demain;

import cc.ifinder.novel.api.domain.user.User;
import cc.ifinder.novel.security.demain.bean.JwtUser;
import cc.ifinder.novel.security.demain.bean.UserRole;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: Richard on
 * 2017/12/5
 * desc:
 */
public class JwtUserFactory {
    private JwtUserFactory() {
    }

    public static JwtUser create(User user) {
        //UniqueName 作为唯一值,UserDetails的name，并且加密作为密码，
        return new JwtUser(
                user.getId(),
                user.getUniqueName(),
                user.getPassword(),
                mapToGrantedAuthorities(user.getUserRoles()),
                user.getLastPasswordResetDate()
        );
    }

    private static List<GrantedAuthority> mapToGrantedAuthorities(List<UserRole> authorities) {
        List<GrantedAuthority> auths = new ArrayList<>();
        for(UserRole userRole : authorities){
            auths.add(new SimpleGrantedAuthority(userRole.getName()));
        }
        return auths;
//        return authorities.stream()
//                .map(SimpleGrantedAuthority::new)
//                .collect(Collectors.toList());
    }
}
