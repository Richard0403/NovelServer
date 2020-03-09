package cc.ifinder.novel.api.domain.book;

import cc.ifinder.novel.api.domain.common.BaseEntity;
import javax.persistence.*;

/** 章节信息 */
@Entity
@Table
public class BookChapterInfo extends BaseEntity {

    @Id @GeneratedValue private Long id;

    @ManyToOne(
            cascade = {CascadeType.MERGE, CascadeType.REFRESH},
            optional = false) // 可选属性optional=false,表示bookInfo不能为空。删除文章，不影响用户
    @JoinColumn(name = "book_id", nullable = false) // 设置在BookChapterInfo表中的关联字段(外键)
    private BookInfo bookInfo;

    @Column(nullable = false)
    private int chapterNo;

    private String title;

    private int wordNum;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String content;

    public BookChapterInfo() {}

    public BookChapterInfo(BookInfo bookInfo, int chapterNo, String title, int wordNum, Long id) {
        this.bookInfo = bookInfo;
        this.chapterNo = chapterNo;
        this.title = title;
        this.wordNum = wordNum;
        this.id = id;
    }

    public BookChapterInfo(
            BookInfo bookInfo, int chapterNo, String title, int wordNum, String content) {
        this.bookInfo = bookInfo;
        this.chapterNo = chapterNo;
        this.title = title;
        this.wordNum = wordNum;
        this.content = content;
    }

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

    public int getChapterNo() {
        return chapterNo;
    }

    public void setChapterNo(int chapterNo) {
        this.chapterNo = chapterNo;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getWordNum() {
        return wordNum;
    }

    public void setWordNum(int wordNum) {
        this.wordNum = wordNum;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
