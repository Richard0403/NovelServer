package cc.ifinder.novel.api.domain.common.summary;

import cc.ifinder.novel.api.domain.user.User;
import java.util.Date;

public interface DiaryTagSummary {
    Long getId();

    User getUser();

    String getName();

    String getDescription();

    String getPicture();

    int getStatus();

    Date getUpdateTime();

    Date getCreateTime();

    int getDiaryCount();
}
