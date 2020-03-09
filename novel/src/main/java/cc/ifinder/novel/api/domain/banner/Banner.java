package cc.ifinder.novel.api.domain.banner;

import cc.ifinder.novel.api.domain.common.BaseEntity;
import javax.persistence.*;

/** banner */
@Entity
@Table
public class Banner extends BaseEntity {
    @Id @GeneratedValue private Long id;

    @Column(nullable = false)
    private int sort;

    @Column(nullable = false)
    private int type;

    private String description;

    @Column(nullable = false)
    private String cover;

    private String url;

    private Long resourceId;

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Long getResourceId() {
        return resourceId;
    }

    public void setResourceId(Long resourceId) {
        this.resourceId = resourceId;
    }
}
