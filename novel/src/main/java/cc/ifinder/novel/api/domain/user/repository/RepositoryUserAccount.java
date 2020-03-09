package cc.ifinder.novel.api.domain.user.repository;

import cc.ifinder.novel.api.domain.user.User;
import cc.ifinder.novel.api.domain.user.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RepositoryUserAccount extends JpaRepository<UserAccount, Long> {
   UserAccount findByUserAndType(User user, Integer type);
}
