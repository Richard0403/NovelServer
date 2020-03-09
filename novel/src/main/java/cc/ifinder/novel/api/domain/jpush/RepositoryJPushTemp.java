package cc.ifinder.novel.api.domain.jpush;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RepositoryJPushTemp extends JpaRepository<JPushTemplate, Long> {
    JPushTemplate findTopByType(Integer type);
}
