package cc.ifinder.novel.api.domain.comment;


import cc.ifinder.novel.api.domain.common.BaseEntity;
import cc.ifinder.novel.api.domain.user.User;

import javax.persistence.*;

/**
 * by Richard on 2017/8/25
 * desc: 评论
 */
@Entity
@Table
public class Comment extends BaseEntity {

    @Id
    @GeneratedValue
    private Long id;
    @Column(columnDefinition = "TEXT")
    private String content;

    @JoinColumn(columnDefinition = "int COMMENT 'words-findPage")
    private String commentType;

    private Long resourceId;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @JoinColumn(name = "to_comment_id")
    private Long toCommentId;

    @ManyToOne
    @JoinColumn(name = "to_user_id")
    private User toUser;

    public String getCommentType() {
        return commentType;
    }

    public void setCommentType(String commentType) {
        this.commentType = commentType;
    }

    public Comment(){}
    public Comment(String content, Long resourceId, User user, String commentType) {
        this.content = content;
        this.resourceId = resourceId;
        this.user = user;
        this.commentType = commentType;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public User getUser() {
        return user;
    }

    public Long getResourceId() {
        return resourceId;
    }

    public void setResourceId(Long resourceId) {
        this.resourceId = resourceId;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Long getToCommentId() {
        return toCommentId;
    }

    public void setToCommentId(Long toCommentId) {
        this.toCommentId = toCommentId;
    }

    public User getToUser() {
        return toUser;
    }

    public void setToUser(User toUser) {
        this.toUser = toUser;
    }

}
