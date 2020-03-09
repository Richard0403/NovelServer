package cc.ifinder.novel.security.demain;

import cc.ifinder.novel.security.demain.bean.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * by Richard on 2017/9/10
 * desc:
 */
public interface RoleRepository extends JpaRepository<UserRole,Long> {
    UserRole findByName(String name);
}
