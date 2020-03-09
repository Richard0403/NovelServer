package cc.ifinder.novel.api.service;

import cc.ifinder.novel.api.domain.common.CommonException;
import cc.ifinder.novel.api.domain.user.*;
import cc.ifinder.novel.api.domain.user.repository.*;
import com.qiniu.util.StringUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

/** by Richard on 2017/9/10 desc: */
@Service
public class UserService {
    @Autowired private RepositoryUser repositoryUser;
    @Autowired private RepositoryFollow repositoryFollow;
    @Autowired private RepositoryUserAccount repositoryUserAccount;
    @Autowired private RepositoryUserAccountRecord repositoryUserAccountRecord;
    @Autowired private RepositoryInviteRecord repositoryInviteRecord;

    public User updateUser(User user, String name, String header, int sex) {
        user.setName(name);
        user.setHeader(header);
        user.setSex(sex);
        return repositoryUser.save(user);
    }

    /** 获取指定账户 */
    public UserAccount getUserAccount(User user, int type) {
        return repositoryUserAccount.findByUserAndType(user, type);
    }

    /** 获取指定账户流水记录 */
    public List<UserAccountRecord> getAccountRecord(User user, int type, int pageNo, int pageSize) {
        List<UserAccountRecord> records = new ArrayList<>();
        Pageable pageable = new PageRequest(pageNo, pageSize, Sort.Direction.DESC, "updateTime");
        Page page = repositoryUserAccountRecord.findByUserAndType(user, type, pageable);
        records.addAll(page.getContent());
        return records;
    }

    /** 填写邀请码 */
    public void writeInviteCode(User user, String fromInviteCode) throws CommonException {
        if (StringUtils.isNullOrEmpty(user.getFromInviteCode())) {
            fromInviteCode = fromInviteCode.toUpperCase();
            User fromUser = repositoryUser.findTopByInviteCode(fromInviteCode);
            if (fromUser == null) {
                throw new CommonException("邀请码不存在");
            } else if (fromUser.equals(user)) {
                throw new CommonException("不能邀请自己哟");
            } else {
                user.setFromInviteCode(fromInviteCode);
                repositoryUser.save(user);
                // 保存账户总数
                UserAccount scoreAccount =
                        repositoryUserAccount.findByUserAndType(fromUser, UserAccount.TYPE_SCORE);
                if (scoreAccount == null) {
                    scoreAccount =
                            new UserAccount(
                                    fromUser, UserAccount.TYPE_SCORE, UserAccount.TYPE_SCORE_NAME);
                }
                scoreAccount.setAmount(scoreAccount.getAmount() + UserAccount.INVITE_SCORE);
                repositoryUserAccount.save(scoreAccount);
                // 保存账户记录
                UserAccountRecord accountRecord =
                        new UserAccountRecord(
                                fromUser,
                                UserAccount.TYPE_SCORE,
                                scoreAccount.getAmount(),
                                UserAccount.INVITE_SCORE,
                                "邀请好友奖励",
                                UserAccountRecord.SOURCE_TYPE_INVITE);
                repositoryUserAccountRecord.save(accountRecord);
                // 保存邀请记录
                InviteRecord inviteRecord =
                        new InviteRecord(
                                fromUser, user, UserAccount.TYPE_SCORE, UserAccount.INVITE_SCORE);
                repositoryInviteRecord.save(inviteRecord);
            }
        } else {
            throw new CommonException("已经填写过邀请码");
        }
    }

    /** 获取邀请记录 */
    public List<InviteRecord> getInviteRecord(User user, int pageNo, int pageSize) {
        List<InviteRecord> records = new ArrayList<>();
        Pageable pageable = new PageRequest(pageNo, pageSize, Sort.Direction.DESC, "updateTime");
        Page page = repositoryInviteRecord.findByFromUser(user, pageable);
        records.addAll(page.getContent());
        return records;
    }

    /** 获取邀请总数 */
    public int getInviteCount(User user) {
        return repositoryInviteRecord.countByFromUser(user);
    }

    public boolean addFollow(User user, Long resourceId, int followType) {
        Follow follow = repositoryFollow.findByResourceIdAndUser(resourceId, user);
        if (follow == null) {
            follow = new Follow(user, resourceId, followType);
            repositoryFollow.save(follow);
            return true;
        } else {
            repositoryFollow.delete(follow);
            return false;
        }
    }

    public List<Map> getFollowUsers(User user, int pageNo, int pageSize) {
        Pageable pageable = new PageRequest(pageNo, pageSize, Sort.Direction.ASC, "id");
        Page page = repositoryFollow.getFollowUsers(user.getId(), pageable);
        return page.getContent();
    }

    public List<Map> getCollectDiary(User user, int pageNo, int pageSize) {
        Pageable pageable = new PageRequest(pageNo, pageSize, Sort.Direction.ASC, "id");
        Page page = repositoryFollow.getCollectDiaries(user.getId(), pageable);
        return page.getContent();
    }
}
