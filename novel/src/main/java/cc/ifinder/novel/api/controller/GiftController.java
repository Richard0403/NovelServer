package cc.ifinder.novel.api.controller;

/**
 * by Richard on 2017/9/24
 * desc:
 */

import cc.ifinder.novel.api.domain.common.CommonException;
import cc.ifinder.novel.api.domain.common.RestResult;
import cc.ifinder.novel.api.domain.user.GiftBuyRecord;
import cc.ifinder.novel.api.domain.user.GiftInfo;
import cc.ifinder.novel.api.domain.user.User;
import cc.ifinder.novel.api.service.GiftInfoService;
import cc.ifinder.novel.utils.RestGenerator;
import cc.ifinder.novel.utils.StringUtil;
import cc.ifinder.novel.utils.TokenUtil;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/gift")
public class GiftController {
    @Autowired
    private GiftInfoService giftInfoService;
    @Autowired
    private TokenUtil tokenUtil;

    @ApiOperation(value = "查询礼物列表")
    @RequestMapping(value="/getGiftList", method= RequestMethod.POST)
    public RestResult getGiftList(@RequestBody Map<String, String> params, HttpServletRequest request){
        int pageSize = Integer.parseInt(params.get("pageSize"));
        int pageNo = Integer.parseInt(params.get("pageNo"));
        List<GiftInfo> giftInfos = giftInfoService.getGifiInfos(pageSize,pageNo);
        return RestGenerator.genSuccessResult(giftInfos);
    }

    @ApiOperation(value = "查询礼物详情")
    @RequestMapping(value="/getGiftInfo", method= RequestMethod.POST)
    public RestResult getGiftInfo(@RequestBody Map<String, String> params, HttpServletRequest request){
        Long id = Long.parseLong(params.get("id"));
        GiftInfo giftInfo = giftInfoService.getGiftInfo(id);
        return RestGenerator.genSuccessResult(giftInfo);
    }

    @ApiOperation(value = "兑换礼物")
    @RequestMapping(value="/exchangeGift", method= RequestMethod.POST)
    public RestResult exchangeGift(@RequestBody Map<String, String> params, HttpServletRequest request) throws Exception {
        Long id = Long.parseLong(params.get("id"));
        final User user = tokenUtil.getUserIdFromHttpReq(request);
        giftInfoService.exchangeGift(id, user);
        return RestGenerator.genSuccessResult();
    }

    @ApiOperation(value = "填写收货地址")
    @RequestMapping(value="/addGiftAddr", method= RequestMethod.POST)
    public RestResult addGiftAddr(@RequestBody Map<String, String> params, HttpServletRequest request) throws Exception {
        Long id = Long.parseLong(params.get("id"));
        String deliverName = params.get("name");
        String deliverPhone = params.get("phone");
        String deliverAddr = params.get("addr");
        if(StringUtil.containEmpty(deliverName, deliverPhone, deliverAddr)){
            throw new CommonException("参数不能为空");
        }
        final User user = tokenUtil.getUserIdFromHttpReq(request);
        giftInfoService.addGiftAddr(id, user, deliverName, deliverPhone, deliverAddr);
        return RestGenerator.genSuccessResult();
    }

    @ApiOperation(value = "查询已购礼物列表")
    @RequestMapping(value="/getGiftBuyRecord", method= RequestMethod.POST)
    public RestResult getGiftBuyRecord(@RequestBody Map<String, String> params, HttpServletRequest request){
        int pageSize = Integer.parseInt(params.get("pageSize"));
        int pageNo = Integer.parseInt(params.get("pageNo"));
        final User user = tokenUtil.getUserIdFromHttpReq(request);
        List<GiftBuyRecord> buyRecord = giftInfoService.getGiftBuyRecord(user, pageSize,pageNo);
        return RestGenerator.genSuccessResult(buyRecord);
    }

}
