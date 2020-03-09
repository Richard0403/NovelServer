package cc.ifinder.novel.api.controller;

import cc.ifinder.novel.api.domain.comment.Praise;
import cc.ifinder.novel.api.domain.comment.RepositoryCmt;
import cc.ifinder.novel.api.domain.comment.RepositoryPraise;
import cc.ifinder.novel.api.domain.common.RestResult;
import cc.ifinder.novel.api.domain.jpush.JPushTemplate;
import cc.ifinder.novel.api.domain.jpush.RepositoryJPushTemp;
import cc.ifinder.novel.api.domain.user.User;
import cc.ifinder.novel.api.domain.user.repository.RepositoryUser;
import cc.ifinder.novel.constant.AppConfig;
import cc.ifinder.novel.utils.JPushUtil;
import cc.ifinder.novel.utils.RestGenerator;
import cc.ifinder.novel.utils.TokenUtil;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;


/**
 * @author Richard
 * desc 点赞
 */
@RestController
@RequestMapping(value = "/praise")
public class PraiseController {
    @Autowired
    private TokenUtil tokenUtil;
    @Autowired
    private RepositoryPraise repositoryPraise;
    @Autowired
    private RepositoryJPushTemp repositoryJPushTemp;
    @Autowired
    private RepositoryCmt repositoryCmt;
    @Autowired
    private RepositoryUser repositoryUser;


    @ApiOperation(value = "点赞")
    @RequestMapping(value="/addPraise", method= RequestMethod.POST)
    public RestResult addPraise(@RequestBody Map<String, String> params, HttpServletRequest request){
        final Long resourceId = Long.valueOf(params.get("resourceId"));
        final Integer resourceType = Integer.valueOf(params.get("resourceType"));
        final Long toUserId = Long.valueOf(params.get("toUserId"));
        final User user = tokenUtil.getUserIdFromHttpReq(request);

        Praise praise = repositoryPraise.findFirstByUserAndResourceIdAndResourceType(user, resourceId, resourceType);
        if(praise == null){
            praise = repositoryPraise.save(new Praise(user, resourceId, resourceType));

            User toUser = repositoryUser.findById(toUserId).get();
            JPushTemplate template = repositoryJPushTemp.findTopByType(AppConfig.PUSH.PRAISE);
            JPushUtil.sendToSingle(template, new String[]{toUser.getjPushId()}, user.getName());

            return RestGenerator.genSuccessResult(praise, "点赞成功");
        }else{
            repositoryPraise.delete(praise);
            return RestGenerator.genSuccessResult(null, "取消点赞");
        }
    }

}
