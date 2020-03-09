package cc.ifinder.novel.api.domain.book;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

/** by Richard on 2017/9/10 desc: */
public interface RepositoryBookShelf extends JpaRepository<BookShelf, Long> {
    Page findByUser_Id(Long userId, Pageable pageable);

    BookShelf findFirstByBookInfo_IdAndUser_Id(Long bookId, Long userId);
}
