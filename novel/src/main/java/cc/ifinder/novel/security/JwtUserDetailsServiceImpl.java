package cc.ifinder.novel.security;

import cc.ifinder.novel.api.domain.user.User;
import cc.ifinder.novel.api.domain.user.repository.RepositoryUser;
import cc.ifinder.novel.security.demain.JwtUserFactory;
import cc.ifinder.novel.security.demain.bean.JwtUser;
import cc.ifinder.novel.security.demain.bean.UserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: Richard on
 * 2017/12/5
 * desc:
 */
@Service("jwtUserDetailsServiceImpl")
public class JwtUserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    RepositoryUser repositoryUser;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = repositoryUser.findByUniqueName(username);
        if (user == null) {
            throw new UsernameNotFoundException(String.format("No user found with qqOpenId '%s'.", username));
        } else {
            List<SimpleGrantedAuthority> authorities = new ArrayList<>();
            //用于添加用户的权限。只要把用户权限添加到authorities 就万事大吉。
            for(UserRole role:user.getUserRoles()) {
                authorities.add(new SimpleGrantedAuthority(role.getName()));
            }

            JwtUser jwtUser = JwtUserFactory.create(user);
            jwtUser.setAuthorities(authorities); //设置权限
            return jwtUser;
        }
    }
}
