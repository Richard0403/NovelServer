package cc.ifinder.novel.api.domain.book;

import cc.ifinder.novel.api.domain.common.BaseEntity;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class CategoryChangeLog extends BaseEntity {
    @Id @GeneratedValue private Long id;

    private Long bookId;
    private String bookName;

    private Long userId;
    private String userName;

    private Long categoryCode;
    private String categoryName;

    public CategoryChangeLog() {}

    public CategoryChangeLog(
            Long bookId,
            String bookName,
            Long userId,
            String userName,
            Long categoryCode,
            String categoryName) {
        this.bookId = bookId;
        this.bookName = bookName;
        this.userId = userId;
        this.userName = userName;
        this.categoryCode = categoryCode;
        this.categoryName = categoryName;
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

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Long getCategoryCode() {
        return categoryCode;
    }

    public void setCategoryCode(Long categoryCode) {
        this.categoryCode = categoryCode;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
}
