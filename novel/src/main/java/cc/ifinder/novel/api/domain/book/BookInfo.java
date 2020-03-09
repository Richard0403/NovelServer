package cc.ifinder.novel.api.domain.book;

import cc.ifinder.novel.api.domain.common.BaseEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.List;
import javax.persistence.*;

/** 书籍信息 */
@Entity
@Table
public class BookInfo extends BaseEntity {
    @Id @GeneratedValue private Long id;

    @ManyToOne(
            cascade = {CascadeType.MERGE, CascadeType.REFRESH},
            optional = false) // 可选属性optional=false,表示author不能为空。删除文章，不影响用户
    @JoinColumn(name = "category_code", nullable = false) // 设置在BookInfo表中的关联字段(外键)
    private BookCategory bookCategory;

    @Column(length = 50, nullable = false)
    private String name;

    @Column(length = 50, nullable = false)
    private String author;

    @Column(columnDefinition = "text")
    private String summary;

    @Column(nullable = false)
    private String cover;

    private int chapter_num = 0;

    private long word_num = 0;

    private String url;

    @Column(nullable = false, columnDefinition = "int COMMENT '1000-图书 2001-视频 2051-音频'")
    private int media_type = 1000;

    private String author_introduction;

    private String poster;

    private long readTimes = 0;

    private String source;

    @JsonIgnore
    @OneToMany(mappedBy = "bookInfo", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    // 级联保存、更新、删除、刷新;延迟加载。当删除分类，会级联删除该分类下所有的书籍
    // 拥有mappedBy注解的实体类为关系被维护端
    // mappedBy=”category_code”中的category_code是BookInfo中的category_code属性
    private List<BookChapterInfo> chapterInfoList; // 目录列表

    public BookInfo(
            BookCategory bookCategory, String name, String author, String summary, String cover) {
        this.bookCategory = bookCategory;
        this.name = name;
        this.author = author;
        this.summary = summary;
        this.cover = cover;
    }

    public BookInfo() {}

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public List<BookChapterInfo> getChapterInfoList() {
        return chapterInfoList;
    }

    public void setChapterInfoList(List<BookChapterInfo> chapterInfoList) {
        this.chapterInfoList = chapterInfoList;
    }

    public Long getReadTimes() {
        return readTimes;
    }

    public void setReadTimes(Long readTimes) {
        this.readTimes = readTimes;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BookCategory getBookCategory() {
        return bookCategory;
    }

    public void setBookCategory(BookCategory bookCategory) {
        this.bookCategory = bookCategory;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public int getChapter_num() {
        return chapter_num;
    }

    public void setChapter_num(int chapter_num) {
        this.chapter_num = chapter_num;
    }

    public Long getWord_num() {
        return word_num;
    }

    public void setWord_num(Long word_num) {
        this.word_num = word_num;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getMedia_type() {
        return media_type;
    }

    public void setMedia_type(int media_type) {
        this.media_type = media_type;
    }

    public String getAuthor_introduction() {
        return author_introduction;
    }

    public void setAuthor_introduction(String author_introduction) {
        this.author_introduction = author_introduction;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }
}
