package cc.ifinder.novel.api.service;

import cc.ifinder.novel.api.domain.common.CommonException;
import cc.ifinder.novel.api.domain.user.*;
import cc.ifinder.novel.api.domain.user.repository.RepositoryGiftBuyRecord;
import cc.ifinder.novel.api.domain.user.repository.RepositoryGiftInfo;
import cc.ifinder.novel.api.domain.user.repository.RepositoryUserAccount;
import cc.ifinder.novel.api.domain.user.repository.RepositoryUserAccountRecord;
import com.qiniu.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


/**
 * by Richard on 2017/9/10
 * desc:
 */
@Service
public class GiftInfoService {
    @Autowired
    RepositoryGiftInfo repGiftInfo;
    @Autowired
    RepositoryUserAccount repUserAccount;
    @Autowired
    RepositoryUserAccountRecord repAccountRecord;
    @Autowired
    RepositoryGiftBuyRecord repGiftBuyRecord;


    public List<GiftInfo> getGifiInfos(int pageSize, int pageNo){
        List<GiftInfo> giftInfos = new ArrayList<>();
        Pageable pageable = new PageRequest(pageNo, pageSize, Sort.Direction.DESC, "updateTime");
        Page page = repGiftInfo.findAll(pageable);
        giftInfos.addAll(page.getContent());
        return giftInfos;
    }

    public GiftInfo getGiftInfo(Long id){
        return repGiftInfo.findById(id).get();
    }


    /**
     * 兑换礼物
     */
    public void exchangeGift(Long giftId, User user) throws Exception {

        GiftInfo giftInfo = repGiftInfo.findById(giftId).get();
        UserAccount account = repUserAccount.findByUserAndType(user, UserAccount.TYPE_SCORE);
        if(account!=null && giftInfo.getPrice()<=account.getAmount()){//账户存在，余额充足
            //-->余额减少
            account.setAmount(account.getAmount()-giftInfo.getPrice());
            repUserAccount.save(account);
            //-->余额记录
            UserAccountRecord accountRecord = new UserAccountRecord(user, UserAccount.TYPE_SCORE, account.getAmount(),
                    -giftInfo.getPrice(), "兑换"+giftInfo.getName(), UserAccountRecord.SOURCE_TYPE_BUY);
            repAccountRecord.save(accountRecord);
            //-->兑换记录
            GiftBuyRecord buyRecord = new GiftBuyRecord(user, giftInfo, GiftBuyRecord.ORDER_STATUS_PAYED, giftInfo.getPrice());
            repGiftBuyRecord.save(buyRecord);
            //-->计数加1
            giftInfo.setOrderNum(giftInfo.getOrderNum()+1);
            repGiftInfo.save(giftInfo);

        }else{
            throw new CommonException("余额不足");
        }
    }

    /**
     * 填写收货地址
     */
    public void addGiftAddr(Long giftRecordId, User user, String deliverName, String deliverPhone, String deliverAddr) throws CommonException {
        GiftBuyRecord buyRecord = repGiftBuyRecord.findById(giftRecordId).get();
        if(user.equals(buyRecord.getUser())){
            if(StringUtils.isNullOrEmpty(buyRecord.getDeliverName())){
                buyRecord.setDeliverAddr(deliverAddr);
                buyRecord.setDeliverName(deliverName);
                buyRecord.setDeliverPhone(deliverPhone);
                buyRecord.setOrderStatus(GiftBuyRecord.ORDER_STATUS_ADD_ADDR);
                repGiftBuyRecord.save(buyRecord);
            }else{
                throw new CommonException("已经填过地址，不允许修改");
            }
        }else{
            throw new CommonException("无权操作");
        }

    }

    /**
     * 已购礼物列表
     */
    public List<GiftBuyRecord> getGiftBuyRecord(User user, int pageSize, int pageNo) {
        List<GiftBuyRecord> buyRecord = new ArrayList<>();
        Pageable pageable = new PageRequest(pageNo, pageSize, Sort.Direction.DESC, "updateTime");
        Page page = repGiftBuyRecord.findByUser(user, pageable);
        buyRecord.addAll(page.getContent());
        return buyRecord;
    }
}
