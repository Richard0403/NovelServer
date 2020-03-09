package cc.ifinder.novel.api.domain.book;

import cc.ifinder.novel.api.domain.common.BaseEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.List;
import javax.persistence.*;

/** by Richard on 2017/8/25 desc: 分类 */
@Entity
@Table
public class BookCategory extends BaseEntity {

    @Id @GeneratedValue private Long code;

    private Long parent_code;

    @Column(length = 10)
    private String name;

    private String comment;

    private String icon;

    @Column(nullable = false, columnDefinition = "int COMMENT '1000-图书 2001-视频 2051-音频'")
    private int media_type;

    @JsonIgnore
    @OneToMany(mappedBy = "bookCategory", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    // 级联保存、更新、删除、刷新;延迟加载。当删除分类，会级联删除该分类下所有的书籍
    // 拥有mappedBy注解的实体类为关系被维护端
    // mappedBy=”category_code”中的category_code是BookInfo中的category_code属性
    private List<BookInfo> bookInfoList; // 书籍列表

    public BookCategory() {}

    public List<BookInfo> getBookInfoList() {
        return bookInfoList;
    }

    public void setBookInfoList(List<BookInfo> bookInfoList) {
        this.bookInfoList = bookInfoList;
    }

    public Long getCode() {
        return code;
    }

    public void setCode(Long code) {
        this.code = code;
    }

    public Long getParent_code() {
        return parent_code;
    }

    public void setParent_code(Long parent_code) {
        this.parent_code = parent_code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public int getMedia_type() {
        return media_type;
    }

    public void setMedia_type(int media_type) {
        this.media_type = media_type;
    }
}
