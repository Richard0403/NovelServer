package cc.ifinder.novel.api.domain.user.repository;

import cc.ifinder.novel.api.domain.user.GiftBuyRecord;
import cc.ifinder.novel.api.domain.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RepositoryGiftBuyRecord extends JpaRepository<GiftBuyRecord, Long> {
    Page findByUser(User user, Pageable pageable);
}
