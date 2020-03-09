package cc.ifinder.novel.api.domain.comment;

import cc.ifinder.novel.api.domain.user.User;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

/** by Richard on 2017/9/10 desc: */
public interface RepositoryCmt extends JpaRepository<Comment, Long> {
    Page findCommentByResourceIdAndStatusNotAndToCommentIdNotNull(
            Long diaryId, int status, Pageable pageable);

    Page findCommentByResourceIdAndStatusNot(Long diaryId, int status, Pageable pageable);

    Page findByCommentTypeAndToCommentIdAndToUserOrderByCreateTimeDesc(
            String type, Long toCommentId, User toUser, Pageable pageable);

    List findByToCommentIdOrderByCreateTimeAsc(Long toCommentId);
}
