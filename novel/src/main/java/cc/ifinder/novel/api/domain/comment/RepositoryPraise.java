package cc.ifinder.novel.api.domain.comment;

import cc.ifinder.novel.api.domain.user.User;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

/** by Richard on 2017/9/10 desc: */
public interface RepositoryPraise extends JpaRepository<Praise, Long> {
    Praise findFirstByUserAndResourceIdAndResourceType(
            User user, Long resourceId, int resourceType);

    List findByResourceIdAndResourceType(Long resourceId, int resourceType);

    int countByResourceIdAndResourceType(Long resourceId, int resourceType);
}
