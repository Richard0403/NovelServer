package cc.ifinder.novel.api.controller;

import cc.ifinder.novel.api.domain.comment.Comment;
import cc.ifinder.novel.api.domain.comment.RepositoryCmt;
import cc.ifinder.novel.api.domain.common.RestResult;
import cc.ifinder.novel.api.domain.user.User;
import cc.ifinder.novel.api.service.CmtService;
import cc.ifinder.novel.constant.AppConfig;
import cc.ifinder.novel.security.demain.bean.UserRole;
import cc.ifinder.novel.utils.RestGenerator;
import cc.ifinder.novel.utils.TokenUtil;
import com.qiniu.util.StringUtils;
import io.swagger.annotations.ApiOperation;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/** @author Richard desc 评论 */
@RestController
@RequestMapping(value = "/comment")
public class CommentController {
    @Autowired private TokenUtil tokenUtil;
    @Autowired private CmtService cmtService;
    @Autowired private RepositoryCmt repositoryCmt;

    @ApiOperation(value = "添加评论， 回复评论")
    @RequestMapping(value = "/addComment", method = RequestMethod.POST)
    public RestResult addComment(
            @RequestBody Map<String, String> params, HttpServletRequest request) {
        final String content = params.get("content");
        final String resourceId = params.get("resourceId");
        final String toUserId = params.get("toUserId");
        final String toCommentId = params.get("toCommentId");
        final String commentType = params.get("commentType");
        final User user = tokenUtil.getUserIdFromHttpReq(request);

        Long resource = StringUtils.isNullOrEmpty(resourceId) ? null : Long.parseLong(resourceId);
        Comment comment =
                cmtService.addComment(resource, content, user, toCommentId, toUserId, commentType);
        return RestGenerator.genSuccessResult(comment, "发布成功");
    }

    @ApiOperation(value = "获取评论列表")
    @RequestMapping(value = "/getComment", method = RequestMethod.POST)
    public RestResult commentDiary(
            @RequestBody Map<String, String> params, HttpServletRequest request) {
        String resourceId = params.get("resourceId");
        String commentType = params.get("commentType");
        int pageSize = Integer.parseInt(params.get("pageSize"));
        int pageNo = Integer.parseInt(params.get("pageNo"));
        final User user = tokenUtil.getUserIdFromHttpReq(request);

        Long resoure = StringUtils.isNullOrEmpty(resourceId) ? null : Long.parseLong(resourceId);
        List<Map<String, Object>> comments =
                cmtService.getComment(user, pageSize, pageNo, commentType, resoure);
        return RestGenerator.genSuccessResult(comments, "获取评论成功");
    }

    @ApiOperation(value = "删除评论")
    @RequestMapping(value = "/delComment", method = RequestMethod.POST)
    public RestResult delComment(
            @RequestBody Map<String, String> params, HttpServletRequest request) {
        final Long commentId = Long.parseLong(params.get("id"));
        Comment comment = repositoryCmt.findById(commentId).get();
        User user = tokenUtil.getUserIdFromHttpReq(request);
        boolean isAdmin = false;
        for (UserRole role : user.getUserRoles()) {
            if (AppConfig.Role.USER_RULE_ADMIN.equals(role.getName())) {
                isAdmin = true;
            }
        }
        if (comment.getUser().equals(user) || isAdmin) {
            cmtService.delComment(comment);
            return RestGenerator.genSuccessResult(null, "删除成功");
        } else {
            return RestGenerator.genErrorResult("无权操作");
        }
    }
}
