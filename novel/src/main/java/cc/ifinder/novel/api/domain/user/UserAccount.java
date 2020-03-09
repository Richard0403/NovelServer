package cc.ifinder.novel.api.domain.user;

import cc.ifinder.novel.api.domain.common.BaseEntity;
import java.io.Serializable;
import javax.persistence.*;

/** 用户信息 */
@Entity
public class UserAccount extends BaseEntity implements Serializable {
    public static final Integer MAX_DAILY_SCORE = 12000;
    public static final Integer INVITE_SCORE = 20000;

    public static final Integer TYPE_SCORE = 0X01;
    public static final Integer TYPE_CASH = 0X02;
    public static final Integer TYPE_BONUS = 0X03;

    public static final String TYPE_SCORE_NAME = "积分";
    public static final String TYPE_CASH_NAME = "现金";
    public static final String TYPE_BONUS_NAME = "红包";

    @Id @GeneratedValue private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private Integer type;

    private String name;

    @Column(nullable = false)
    private Integer amount = 0;

    private Integer frozenAmount = 0;

    public UserAccount() {}

    public UserAccount(User user, Integer type, String name) {
        this.user = user;
        this.type = type;
        this.name = name;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public Integer getFrozenAmount() {
        return frozenAmount;
    }

    public void setFrozenAmount(Integer frozenAmount) {
        this.frozenAmount = frozenAmount;
    }
}
