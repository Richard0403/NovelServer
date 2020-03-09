package cc.ifinder.novel.security.auth;


import cc.ifinder.novel.api.domain.common.RestResult;
import cc.ifinder.novel.api.domain.user.User;

public interface AuthService {
    RestResult register(User userToAdd);
    String login(String username, String password);
    String refresh(String oldToken);
}
