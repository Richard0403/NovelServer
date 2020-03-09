package cc.ifinder.novel.api.domain.user.repository;

import cc.ifinder.novel.api.domain.user.Follow;
import cc.ifinder.novel.api.domain.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface RepositoryFollow extends JpaRepository<Follow, Long> {
    Follow findByResourceIdAndUser(Long resourceId, User user);

    @Query(
            value =
                    "select * from follow f "
                            + "left join user u on f.resource_id=u.id  "
                            + "where f.user_id = ?1"
                            + "ORDER BY ?#{#pageable}",
            countQuery = "select count(*) from follow  where user_id = ?1",
            nativeQuery = true)
    Page getFollowUsers(Long userId, Pageable pageable);

    @Query(
            value =
                    "select * from follow f "
                            + "left join diary d on f.resource_id=d.id  "
                            + "where f.user_id = ?1"
                            + "ORDER BY ?#{#pageable}",
            countQuery = "select count(*) from follow where user_id = ?1",
            nativeQuery = true)
    Page getCollectDiaries(Long userId, Pageable pageable);
}
