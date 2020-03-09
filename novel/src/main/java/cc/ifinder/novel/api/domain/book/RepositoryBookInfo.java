package cc.ifinder.novel.api.domain.book;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

/** by Richard on 2017/9/10 desc: */
public interface RepositoryBookInfo extends JpaRepository<BookInfo, Long> {
    Page findByBookCategory_Code(Long categoryCode, Pageable pageable);

    Page findAll(Pageable pageable);

    Page findByNameLikeOrAuthorLike(String keywords, String keyword2, Pageable pageable);

    BookInfo findTopByNameAndAuthor(String name, String author);
}
