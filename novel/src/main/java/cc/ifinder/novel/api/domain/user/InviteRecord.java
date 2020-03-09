package cc.ifinder.novel.api.domain.user;

import cc.ifinder.novel.api.domain.common.BaseEntity;
import java.io.Serializable;
import javax.persistence.*;

/** 邀请记录 */
@Entity
public class InviteRecord extends BaseEntity implements Serializable {

    @Id @GeneratedValue private Long id;

    @ManyToOne
    @JoinColumn(name = "from_user_id", nullable = false)
    private User fromUser;

    @ManyToOne
    @JoinColumn(name = "to_user_id", nullable = false)
    private User toUser;

    @Column(nullable = false, columnDefinition = "int COMMENT '奖励货币的类型'")
    private Integer accountType;

    @Column(nullable = false, columnDefinition = "int COMMENT '奖励的金额数量'")
    private Integer amount;

    public InviteRecord() {}

    public InviteRecord(User fromUser, User toUser, Integer accountType, Integer amount) {
        this.fromUser = fromUser;
        this.toUser = toUser;
        this.accountType = accountType;
        this.amount = amount;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getFromUser() {
        return fromUser;
    }

    public void setFromUser(User fromUser) {
        this.fromUser = fromUser;
    }

    public User getToUser() {
        return toUser;
    }

    public void setToUser(User toUser) {
        this.toUser = toUser;
    }

    public Integer getAccountType() {
        return accountType;
    }

    public void setAccountType(Integer accountType) {
        this.accountType = accountType;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }
}
