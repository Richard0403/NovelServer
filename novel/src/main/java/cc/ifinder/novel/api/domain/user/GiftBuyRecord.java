package cc.ifinder.novel.api.domain.user;

import cc.ifinder.novel.api.domain.common.BaseEntity;
import java.io.Serializable;
import javax.persistence.*;

/** 兑换记录 */
@Entity
public class GiftBuyRecord extends BaseEntity implements Serializable {
    public static final int ORDER_STATUS_UNPAY = 0X01;
    public static final int ORDER_STATUS_PAYED = 0X02;
    public static final int ORDER_STATUS_ADD_ADDR = 0X03;
    public static final int ORDER_STATUS_DELIVERED = 0X04;
    public static final int ORDER_STATUS_COMPLETE = 0X05;
    public static final int ORDER_STATUS_CANCEL = 0X06;

    @Id @GeneratedValue private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "gift_info_id", nullable = false)
    private GiftInfo giftInfo;

    @JoinColumn(nullable = false)
    private Integer orderStatus;

    @JoinColumn(nullable = false)
    private Integer price;

    private String deliverName;
    private String deliverPhone;
    private String deliverAddr;

    public GiftBuyRecord() {}

    public GiftBuyRecord(User user, GiftInfo giftInfo, Integer orderStatus, Integer price) {
        this.user = user;
        this.giftInfo = giftInfo;
        this.orderStatus = orderStatus;
        this.price = price;
    }

    public String getDeliverName() {
        return deliverName;
    }

    public void setDeliverName(String deliverName) {
        this.deliverName = deliverName;
    }

    public String getDeliverPhone() {
        return deliverPhone;
    }

    public void setDeliverPhone(String deliverPhone) {
        this.deliverPhone = deliverPhone;
    }

    public String getDeliverAddr() {
        return deliverAddr;
    }

    public void setDeliverAddr(String deliverAddr) {
        this.deliverAddr = deliverAddr;
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

    public GiftInfo getGiftInfo() {
        return giftInfo;
    }

    public void setGiftInfo(GiftInfo giftInfo) {
        this.giftInfo = giftInfo;
    }

    public Integer getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(Integer orderStatus) {
        this.orderStatus = orderStatus;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }
}
