package cc.ifinder.novel.api.domain.book;

import cc.ifinder.novel.api.domain.common.summary.ReadTimeSummary;
import cc.ifinder.novel.api.domain.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;


/**
 * by Richard on 2017/9/10
 * desc:
 */
public interface RepositoryTimeRecord extends JpaRepository<TimeRecord,Long> {


    /**
     * 获取日常阅读时间
     */
    @Query("select " +
            "substring(record.createTime,1,10) as date, " +
            "count(record) as times, " +
            "sum(record.readTime) as readTime " +
            "from TimeRecord record " +
            "where record.user = ?1 " +
            "group by substring(record.createTime,1,10) "+
            "order by substring(record.createTime,1,10)")
    Page<ReadTimeSummary> getDailySum(User user, Pageable pageable);

    /**
     * 获取当日阅读时间
     */
    @Query("select " +
            "substring(record.createTime,1,4) as date, " +
            "count(record) as times, " +
            "sum(record.readTime) as readTime " +
            "from TimeRecord record " +
            "where record.user = ?1 and record.createTime>?2 and record.createTime<?3 " +
            "group by substring(record.createTime,1,4) ")
    ReadTimeSummary getDaySum(User user, Date startTime, Date endTime);

}
