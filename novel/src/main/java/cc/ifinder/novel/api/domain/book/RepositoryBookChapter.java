package cc.ifinder.novel.api.domain.book;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/** by Richard on 2017/9/10 desc: */
public interface RepositoryBookChapter extends JpaRepository<BookChapterInfo, Long> {

    @Query(
            "select new BookChapterInfo(chapter.bookInfo, chapter.chapterNo,chapter.title,chapter.wordNum,chapter.id)"
                    + " FROM BookChapterInfo chapter where chapter.bookInfo.id=:bookId order by chapter.chapterNo")
    List<BookChapterInfo> findByBookInfo_IdOrderByChapterNo(@Param("bookId") Long bookId);

    BookChapterInfo getById(Long chapterId);

    BookChapterInfo findTopByBookInfo_IdAndChapterNo(Long bookId, int chapterNo);
}
