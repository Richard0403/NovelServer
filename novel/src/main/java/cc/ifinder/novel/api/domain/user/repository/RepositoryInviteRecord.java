package cc.ifinder.novel.api.domain.user.repository;

import cc.ifinder.novel.api.domain.user.InviteRecord;
import cc.ifinder.novel.api.domain.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RepositoryInviteRecord extends JpaRepository<InviteRecord, Long> {
    Page findByFromUser(User fromUser, Pageable pageable);
    int countByFromUser(User fromUser);
}
