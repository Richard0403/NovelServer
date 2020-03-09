package cc.ifinder.novel.api.domain.book;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

/**
 * by Richard on 2017/9/10
 * desc:
 */
public interface RepositoryCategoryChangeLog extends JpaRepository<CategoryChangeLog,Long> {
    /**
     * 查询该本书的修改记录
     */
    Page getCategoryChangeLogByBookIdOrderByUpdateTimeAsc(Long bookId, Pageable pageable);

    /**
     * 查询指定date到现在的userId的修改记录
     * @param date
     * @return
     */
    List<CategoryChangeLog> getCategoryChangeLogByUserIdAndCreateTimeAfterOrderByCreateTime(Long userId, Date date);
}
