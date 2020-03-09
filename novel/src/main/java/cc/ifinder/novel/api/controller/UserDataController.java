package cc.ifinder.novel.api.controller;

import cc.ifinder.novel.api.domain.common.RestResult;
import cc.ifinder.novel.api.domain.common.summary.ReadTimeSummary;
import cc.ifinder.novel.api.domain.user.User;
import cc.ifinder.novel.api.service.UserDataService;
import cc.ifinder.novel.utils.RestGenerator;
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


/**
 * @author Richard
 * desc 用户信息相关
 */
@RestController
@RequestMapping(value = "/userdata")
public class UserDataController {
    @Autowired
    private TokenUtil tokenUtil;
    @Autowired
    private UserDataService dataService;

    @ApiOperation(value = "提交阅读时间记录")
    @RequestMapping(value="/uploadReadTime", method= RequestMethod.POST)
    public RestResult uploadReadTime(@RequestBody Map<String, Object> params, HttpServletRequest request){
        final User user = tokenUtil.getUserIdFromHttpReq(request);
        final String readRecordsStr = String.valueOf(params.get("readRecords"));
        dataService.saveReadTimeRecord(user, readRecordsStr);
        return RestGenerator.genSuccessResult();
    }

    @ApiOperation(value = "按天查询阅读时间")
    @RequestMapping(value="/getDailyReadTime", method= RequestMethod.POST)
    public RestResult getDailyReadTime(@RequestBody Map<String, String> params, HttpServletRequest request){
        final User user = tokenUtil.getUserIdFromHttpReq(request);
        int pageSize = Integer.parseInt(params.get("pageSize"));
        int pageNo = Integer.parseInt(params.get("pageNo"));
        List<ReadTimeSummary> timeSummaries = dataService.getDailyReadTime(user, pageNo, pageSize);
        return RestGenerator.genSuccessResult(timeSummaries);
    }

    @ApiOperation(value = "查询某一天的阅读时间")
    @RequestMapping(value="/getDayReadTime", method= RequestMethod.POST)
    public RestResult getDayReadTime(@RequestBody Map<String, String> params, HttpServletRequest request){
        final User user = tokenUtil.getUserIdFromHttpReq(request);
        Long time = Long.parseLong(params.get("time"));
        ReadTimeSummary timeSummary = dataService.getDayReadTime(user, time);
        return RestGenerator.genSuccessResult(timeSummary);
    }
}
