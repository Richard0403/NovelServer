package cc.ifinder.novel.api.domain.version;

import org.springframework.data.jpa.repository.JpaRepository;

public interface RepositoryVersion extends JpaRepository<Version, Long> {
    Version findTopByOrderByUpdateTimeDesc();
    Version findByVersionName(String name);
}
