package cc.ifinder.novel.api.domain.book;

import org.springframework.data.jpa.repository.JpaRepository;

/** by Richard on 2017/9/10 desc: */
public interface RepositoryBookCategory extends JpaRepository<BookCategory, Long> {
    BookCategory findTopByCode(Long code);
}
