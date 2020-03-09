package cc.ifinder.novel.api.domain.book;

import cc.ifinder.novel.api.domain.common.BaseEntity;
import cc.ifinder.novel.api.domain.user.User;
import javax.persistence.*;

/** by Richard on 2017/8/25 desc: 阅读时间记录 */
@Entity
@Table
public class TimeRecord extends BaseEntity {

    @Id @GeneratedValue private Long id;

    private Long bookId;
    private Long readTime;
    private Long saveTime; // 保存的时刻-->前端决定

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getBookId() {
        return bookId;
    }

    public void setBookId(Long bookId) {
        this.bookId = bookId;
    }

    public Long getReadTime() {
        return readTime;
    }

    public void setReadTime(Long readTime) {
        this.readTime = readTime;
    }

    public Long getSaveTime() {
        return saveTime;
    }

    public void setSaveTime(Long saveTime) {
        this.saveTime = saveTime;
    }
}
