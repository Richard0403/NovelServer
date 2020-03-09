package cc.ifinder.novel.api.service;

import cc.ifinder.novel.api.domain.book.RepositoryTimeRecord;
import cc.ifinder.novel.api.domain.book.TimeRecord;
import cc.ifinder.novel.api.domain.common.summary.ReadTimeSummary;
import cc.ifinder.novel.api.domain.user.User;
import cc.ifinder.novel.api.domain.user.UserAccount;
import cc.ifinder.novel.api.domain.user.UserAccountRecord;
import cc.ifinder.novel.api.domain.user.repository.RepositoryUserAccount;
import cc.ifinder.novel.api.domain.user.repository.RepositoryUserAccountRecord;
import cc.ifinder.novel.utils.TimeUtil;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

/** by Richard on 2017/9/10 desc: */
@Service
public class UserDataService {
    @Autowired private RepositoryTimeRecord repositoryTimeRecord;
    @Autowired private RepositoryUserAccount repositoryUserAccount;
    @Autowired private RepositoryUserAccountRecord repositoryUserAccountRecord;

    /** 保存阅读时间 */
    public void saveReadTimeRecord(User user, String readTimeRecordsStr) {
        int score = 0;

        List<TimeRecord> records =
                new Gson()
                        .fromJson(
                                readTimeRecordsStr, new TypeToken<List<TimeRecord>>() {}.getType());
        for (int i = 0; i < records.size(); i++) {
            records.get(i).setUser(user);
            score += (100 * records.get(i).getReadTime() / 60); // 积分单位为"分"
        }
        repositoryTimeRecord.saveAll(records);
        // 计算通过阅读获得的积分，是否超出当日上限
        Integer todayCount =
                repositoryUserAccountRecord.getSumByTypeAndUserAndSourceTypeAndTime(
                        user,
                        UserAccount.TYPE_SCORE,
                        UserAccountRecord.SOURCE_TYPE_READ,
                        TimeUtil.getDayStart(System.currentTimeMillis()),
                        TimeUtil.getDayEnd(System.currentTimeMillis()));
        todayCount = todayCount == null ? 0 : todayCount; // 可能空
        if (score + todayCount > UserAccount.MAX_DAILY_SCORE) { // 超出最大值
            score = UserAccount.MAX_DAILY_SCORE - todayCount;
            score = score > 0 ? score : 0; // 负数置零
        }

        if (score > 0) {
            // 保存账户总数
            UserAccount scoreAccount =
                    repositoryUserAccount.findByUserAndType(user, UserAccount.TYPE_SCORE);
            if (scoreAccount == null) {
                scoreAccount =
                        new UserAccount(user, UserAccount.TYPE_SCORE, UserAccount.TYPE_SCORE_NAME);
            }
            scoreAccount.setAmount(scoreAccount.getAmount() + score);
            repositoryUserAccount.save(scoreAccount);
            // 保存账户记录
            UserAccountRecord accountRecord =
                    new UserAccountRecord(
                            user,
                            UserAccount.TYPE_SCORE,
                            scoreAccount.getAmount(),
                            score,
                            "阅读时长奖励",
                            UserAccountRecord.SOURCE_TYPE_READ);
            repositoryUserAccountRecord.save(accountRecord);
        }
    }

    /** 查询日常阅读时间 */
    public List<ReadTimeSummary> getDailyReadTime(User user, int pageNo, int pageSize) {
        Pageable pageable = new PageRequest(pageNo, pageSize, Sort.Direction.DESC, "id");
        Page<ReadTimeSummary> page = repositoryTimeRecord.getDailySum(user, pageable);
        return page.getContent();
    }
    /** 查询当天的阅读时间 */
    public ReadTimeSummary getDayReadTime(User user, Long time) {
        ReadTimeSummary timeSummary =
                repositoryTimeRecord.getDaySum(
                        user, TimeUtil.getDayStart(time), TimeUtil.getDayEnd(time));
        return timeSummary;
    }
}
