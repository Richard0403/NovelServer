package cc.ifinder.novel.api.domain.user;

import cc.ifinder.novel.api.domain.common.BaseEntity;
import java.io.Serializable;
import javax.persistence.*;

/** 用户信息 */
@Entity
public class UserAccountRecord extends BaseEntity implements Serializable {
    public static final int SOURCE_TYPE_READ = 0X01;
    public static final int SOURCE_TYPE_INVITE = 0X02;
    public static final int SOURCE_TYPE_REWARD = 0X03;
    public static final int SOURCE_TYPE_BUY = 0x04;

    @Id @GeneratedValue private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private Integer type;

    @Column(nullable = false, columnDefinition = "int COMMENT '变化后的总额'")
    private Integer amount;

    @Column(nullable = false, columnDefinition = "int COMMENT '额度变化'")
    private Integer amountChange;

    @Column(nullable = false, columnDefinition = "varchar(50) COMMENT '来源描述'")
    private String title;

    @Column(nullable = false, columnDefinition = "int COMMENT '来源类型'")
    private Integer sourceType = SOURCE_TYPE_READ;

    public UserAccountRecord() {}

    public UserAccountRecord(
            User user,
            Integer type,
            Integer amount,
            Integer amountChange,
            String title,
            Integer sourceType) {
        this.user = user;
        this.type = type;
        this.amount = amount;
        this.amountChange = amountChange;
        this.title = title;
        this.sourceType = sourceType;
    }

    public Integer getSourceType() {
        return sourceType;
    }

    public void setSourceType(Integer sourceType) {
        this.sourceType = sourceType;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public Integer getAmountChange() {
        return amountChange;
    }

    public void setAmountChange(Integer amountChange) {
        this.amountChange = amountChange;
    }
}
