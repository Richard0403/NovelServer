package cc.ifinder.novel.api.domain.banner;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * by Richard on 2017/9/10
 * desc:
 */
public interface RepositoryBanner extends JpaRepository<Banner,Long> {
    List<Banner> findAll();
    List<Banner> findAllBySort(int sort);
}
