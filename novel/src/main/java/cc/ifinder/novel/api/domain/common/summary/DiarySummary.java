package cc.ifinder.novel.api.domain.common.summary;

import cc.ifinder.novel.api.domain.user.User;
import java.util.Date;

public interface DiarySummary {
    Long getId();

    String getTitle();

    String getContent();

    User getUser();

    String getPicture();

    int getStatus();

    Date getUpdateTime();

    Date getCreateTime();

    int getReadNum();

    int getShareNum();

    int getIsPraise();
}
