package cc.ifinder.novel.api.domain.book;

import cc.ifinder.novel.api.domain.common.BaseEntity;
import cc.ifinder.novel.api.domain.user.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.Date;
import javax.persistence.*;

@Entity
public class BookShelf extends BaseEntity {
    @Id @GeneratedValue private Long id;

    @ManyToOne(
            cascade = {CascadeType.MERGE, CascadeType.REFRESH},
            optional = false)
    @JoinColumn(name = "book_id")
    private BookInfo bookInfo;

    @ManyToOne(
            cascade = {CascadeType.MERGE, CascadeType.REFRESH},
            optional = false)
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;

    private int chapterPos = 0;

    private int pagePos = 0;

    private Date lastReadTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BookInfo getBookInfo() {
        return bookInfo;
    }

    public void setBookInfo(BookInfo bookInfo) {
        this.bookInfo = bookInfo;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getChapterPos() {
        return chapterPos;
    }

    public void setChapterPos(int chapterPos) {
        this.chapterPos = chapterPos;
    }

    public int getPagePos() {
        return pagePos;
    }

    public void setPagePos(int pagePos) {
        this.pagePos = pagePos;
    }

    public Date getLastReadTime() {
        return lastReadTime;
    }

    public void setLastReadTime(Date lastReadTime) {
        this.lastReadTime = lastReadTime;
    }
}
