package cc.ifinder.novel.api.controller;

import cc.ifinder.novel.api.domain.common.CommonException;
import cc.ifinder.novel.api.domain.common.RestResult;
import cc.ifinder.novel.api.domain.user.InviteRecord;
import cc.ifinder.novel.api.domain.user.User;
import cc.ifinder.novel.api.domain.user.UserAccount;
import cc.ifinder.novel.api.domain.user.UserAccountRecord;
import cc.ifinder.novel.api.service.UserService;
import cc.ifinder.novel.utils.RestGenerator;
import cc.ifinder.novel.utils.TokenUtil;
import io.swagger.annotations.ApiOperation;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/** @author Richard desc 用户信息相关 */
@RestController
@RequestMapping(value = "/user")
public class UserController {
    @Autowired private TokenUtil tokenUtil;
    @Autowired private UserService userService;

    @ApiOperation(value = "修改用户信息")
    @RequestMapping(value = "/updateUser", method = RequestMethod.POST)
    public RestResult updateUser(
            @RequestBody Map<String, Object> params, HttpServletRequest request) {
        final User user = tokenUtil.getUserIdFromHttpReq(request);
        final String name = String.valueOf(params.get("name"));
        final String header = String.valueOf(params.get("header"));
        final int sex = (int) params.get("sex");
        User newUser = userService.updateUser(user, name, header, sex);
        return RestGenerator.genSuccessResult(newUser, "更新成功");
    }

    @ApiOperation(value = "填写邀请码")
    @RequestMapping(value = "/writeInviteCode", method = RequestMethod.POST)
    public RestResult writeInviteCode(
            @RequestBody Map<String, Object> params, HttpServletRequest request)
            throws CommonException {
        final User user = tokenUtil.getUserIdFromHttpReq(request);
        final String inviteCode = String.valueOf(params.get("inviteCode"));
        userService.writeInviteCode(user, inviteCode);
        return RestGenerator.genSuccessResult();
    }

    @ApiOperation(value = "查询指定账户信息")
    @RequestMapping(value = "/getUserAccount", method = RequestMethod.POST)
    public RestResult getUserAccount(
            @RequestBody Map<String, String> params, HttpServletRequest request) {
        Integer type = Integer.valueOf(params.get("type"));
        final User user = tokenUtil.getUserIdFromHttpReq(request);
        UserAccount account = userService.getUserAccount(user, type);
        return RestGenerator.genSuccessResult(account, "查询成功");
    }

    @ApiOperation(value = "查询指定账户流水")
    @RequestMapping(value = "/getAccountRecord", method = RequestMethod.POST)
    public RestResult getAccountRecord(
            @RequestBody Map<String, String> params, HttpServletRequest request) {
        Integer type = Integer.parseInt(params.get("type"));
        Integer pageNo = Integer.parseInt(params.get("pageNo"));
        Integer pageSize = Integer.parseInt(params.get("pageSize"));
        final User user = tokenUtil.getUserIdFromHttpReq(request);
        List<UserAccountRecord> records =
                userService.getAccountRecord(user, type, pageNo, pageSize);
        return RestGenerator.genSuccessResult(records, "查询成功");
    }

    @ApiOperation(value = "邀请记录")
    @RequestMapping(value = "/getInviteRecord", method = RequestMethod.POST)
    public RestResult getInviteRecord(
            @RequestBody Map<String, String> params, HttpServletRequest request) {
        Integer pageNo = Integer.parseInt(params.get("pageNo"));
        Integer pageSize = Integer.parseInt(params.get("pageSize"));
        final User user = tokenUtil.getUserIdFromHttpReq(request);
        List<InviteRecord> records = userService.getInviteRecord(user, pageNo, pageSize);
        return RestGenerator.genSuccessResult(records, "查询成功");
    }

    @ApiOperation(value = "邀请总数")
    @RequestMapping(value = "/getInviteCount", method = RequestMethod.POST)
    public RestResult getInviteCount(
            @RequestBody Map<String, String> params, HttpServletRequest request) {
        final User user = tokenUtil.getUserIdFromHttpReq(request);
        int count = userService.getInviteCount(user);
        return RestGenerator.genSuccessResult(count, "查询成功");
    }

    @ApiOperation(value = "关注 收藏")
    @RequestMapping(value = "/addFollow", method = RequestMethod.POST)
    public RestResult follow(@RequestBody Map<String, String> params, HttpServletRequest request) {
        final Long resourceId = Long.parseLong(params.get("resourceId"));
        final int followType = Integer.parseInt(params.get("followType"));

        final User user = tokenUtil.getUserIdFromHttpReq(request);
        if (userService.addFollow(user, resourceId, followType)) {
            return RestGenerator.genSuccessResult(null, "关注/收藏成功");
        } else {
            return RestGenerator.genSuccessResult(null, "取消关注/收藏");
        }
    }

    @ApiOperation(value = "获取关注列表")
    @RequestMapping(value = "/queryFollowUser", method = RequestMethod.POST)
    public RestResult queryFollowUser(
            @RequestBody Map<String, String> params, HttpServletRequest request) {
        final int pageNo = Integer.parseInt(params.get("pageNo"));
        final int pageSize = Integer.parseInt(params.get("pageSize"));

        final User user = tokenUtil.getUserIdFromHttpReq(request);
        List<Map> follows = userService.getFollowUsers(user, pageNo, pageSize);
        return RestGenerator.genSuccessResult(follows, "获取关注成功");
    }

    @ApiOperation(value = "获取收藏列表")
    @RequestMapping(value = "/queryFollowDiary", method = RequestMethod.POST)
    public RestResult queryFollowDiary(
            @RequestBody Map<String, String> params, HttpServletRequest request) {
        final int pageNo = Integer.parseInt(params.get("pageNo"));
        final int pageSize = Integer.parseInt(params.get("pageSize"));

        final User user = tokenUtil.getUserIdFromHttpReq(request);
        List<Map> diaries = userService.getCollectDiary(user, pageNo, pageSize);
        return RestGenerator.genSuccessResult(diaries, "获取收藏成功");
    }
}
