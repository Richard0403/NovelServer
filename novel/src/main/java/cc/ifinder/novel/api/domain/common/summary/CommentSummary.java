package cc.ifinder.novel.api.domain.common.summary;


import cc.ifinder.novel.api.domain.comment.Comment;

import java.util.List;

public interface CommentSummary {
     Comment comment();
     List<Comment> childComment();
}
