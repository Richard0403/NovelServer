package cc.ifinder.novel.api.domain.comment;

import cc.ifinder.novel.api.domain.common.BaseEntity;
import cc.ifinder.novel.api.domain.user.User;
import javax.persistence.*;

/** by Richard on 2017/8/25 desc: 点赞信息 */
@Entity
@Table
public class Praise extends BaseEntity {
    public static final int TYPE_CMT = 0;

    @Id @GeneratedValue private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private Long resourceId;

    @Column(nullable = false, columnDefinition = "int COMMENT '0-评论'")
    private Integer resourceType;

    public Praise() {}

    public Praise(User user, Long resourceId, Integer resourceType) {
        this.user = user;
        this.resourceId = resourceId;
        this.resourceType = resourceType;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Long getResourceId() {
        return resourceId;
    }

    public void setResourceId(Long resourceId) {
        this.resourceId = resourceId;
    }

    public Integer getResourceType() {
        return resourceType;
    }

    public void setResourceType(Integer resourceType) {
        this.resourceType = resourceType;
    }

    @Override
    public boolean equals(Object obj) {
        if (getId() == (((Praise) obj).getId())) {
            return true;
        } else {
            return false;
        }
    }
}
