package cc.ifinder.novel.api.service;

import cc.ifinder.novel.api.domain.comment.Comment;
import cc.ifinder.novel.api.domain.comment.Praise;
import cc.ifinder.novel.api.domain.comment.RepositoryCmt;
import cc.ifinder.novel.api.domain.comment.RepositoryPraise;
import cc.ifinder.novel.api.domain.jpush.JPushTemplate;
import cc.ifinder.novel.api.domain.jpush.RepositoryJPushTemp;
import cc.ifinder.novel.api.domain.user.User;
import cc.ifinder.novel.api.domain.user.repository.RepositoryUser;
import cc.ifinder.novel.constant.AppConfig;
import cc.ifinder.novel.utils.JPushUtil;
import com.qiniu.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * by Richard on 2017/9/10
 * desc:
 */
@Service
public class CmtService {

    @Autowired
    private RepositoryUser repositoryUser;
    @Autowired
    private RepositoryCmt repositoryCmt;
    @Autowired
    private RepositoryPraise repositoryPraise;
    @Autowired
    private RepositoryJPushTemp repositoryJPushTemp;


    public Comment addComment(Long resourceId, String content, User user, String toCommentId, String toUserId, String commentType) {
        Comment currentCmt = new Comment(content, resourceId, user, commentType);
        if(!StringUtils.isNullOrEmpty(toCommentId)){
            currentCmt.setToCommentId(Long.parseLong(toCommentId));
        }
        if(!StringUtils.isNullOrEmpty(toUserId)){
            User toUser = repositoryUser.findById(Long.parseLong(toUserId));
            currentCmt.setToUser(toUser);

            //发送通知
            JPushTemplate template = repositoryJPushTemp.findTopByType(AppConfig.PUSH.REPLY);
            JPushUtil.sendToSingle(template, new String[]{toUser.getjPushId()}, user.getName(), String.valueOf(resourceId));
        }
        return repositoryCmt.save(currentCmt);
    }

    public List<Map<String,Object>> getComment(User user, int pageSize, int pageNo, String type, Long resourceId){
        List<Comment> comments = new ArrayList<>();
        Pageable pageable = new PageRequest(pageNo, pageSize, Sort.Direction.ASC, "id");
        Page page = repositoryCmt.findByCommentTypeAndToCommentIdAndToUserOrderByCreateTimeDesc(type, resourceId,null, pageable);
        comments.addAll(page.getContent());

        List<Map<String, Object>> resultList = new ArrayList<>();
        for(Comment comment : comments){
            List childCmts = repositoryCmt.findByToCommentIdOrderByCreateTimeAsc(comment.getId());
            int praiseNum = repositoryPraise.countByResourceIdAndResourceType(comment.getId(), Praise.TYPE_CMT);
            Praise userPraise = repositoryPraise.findFirstByUserAndResourceIdAndResourceType(user, comment.getId(), Praise.TYPE_CMT);
            Map<String, Object> map = new HashMap<>();
            map.put("comment", comment);
            map.put("praiseNum", praiseNum);
            map.put("userPraise", userPraise);
            map.put("childCmt", childCmts);
            resultList.add(map);
        }
        return resultList;
    }



    public void delComment(Comment comment) {
        if(comment.getToCommentId() == null){//父评论 --->先删除子评论,再删除自己
            List childCmts = repositoryCmt.findByToCommentIdOrderByCreateTimeAsc(comment.getId());
            repositoryCmt.deleteAll(childCmts);
        }
        repositoryCmt.delete(comment);
    }
}
