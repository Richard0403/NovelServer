package cc.ifinder.novel.api.domain.user.repository;

import cc.ifinder.novel.api.domain.user.GiftInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RepositoryGiftInfo extends JpaRepository<GiftInfo, Long> {
}
