package cc.ifinder.novel.api.domain.user;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

/** 关注 收藏 */
@Entity
public class Follow implements Serializable {
    @Id @GeneratedValue private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private Long resourceId;

    @Column(columnDefinition = "int Comment '0-关注用户，1收藏笔记'")
    private int followType;

    @Column(name = "updateTime")
    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateTime;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(updatable = false)
    @CreationTimestamp
    private Date createTime;

    public Follow() {}

    public Follow(User user, Long resourceId, int followType) {
        this.user = user;
        this.resourceId = resourceId;
        this.followType = followType;
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

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public int getFollowType() {
        return followType;
    }

    public void setFollowType(int followType) {
        this.followType = followType;
    }
}
