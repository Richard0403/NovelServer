package cc.ifinder.novel.api.domain.user;

import cc.ifinder.novel.api.domain.common.BaseEntity;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/** 可兑换的礼物信息 */
@Entity
public class GiftInfo extends BaseEntity implements Serializable {
    @Id @GeneratedValue private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String cover;

    private String detail;

    @Column(nullable = false)
    private Integer price; // 兑换价格

    @Column(nullable = false)
    private Integer marketPrice; // 市场价格

    private Integer orderNum = 0; // 已购人数

    public Integer getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(Integer orderNum) {
        this.orderNum = orderNum;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Integer getMarketPrice() {
        return marketPrice;
    }

    public void setMarketPrice(Integer marketPrice) {
        this.marketPrice = marketPrice;
    }
}
