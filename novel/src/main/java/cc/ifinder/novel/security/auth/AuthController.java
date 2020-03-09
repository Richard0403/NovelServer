package cc.ifinder.novel.security.auth;

import cc.ifinder.novel.api.domain.common.RestResult;
import cc.ifinder.novel.api.domain.user.User;
import cc.ifinder.novel.api.param.P_User;
import cc.ifinder.novel.constant.AppConfig;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Map;

@RestController
@RequestMapping(value = "/auth")
public class AuthController {
    @Value("${jwt.header}")
    private String tokenHeader;

    @Autowired
    private AuthService authService;

    @RequestMapping(value = "/auth", method = RequestMethod.POST)
    public ResponseEntity<?> createAuthenticationToken(
            @RequestBody User authenticationRequest) throws AuthenticationException {
        final String token = authService.login(authenticationRequest.getName(), authenticationRequest.getPassword());
        // Return the token
        return ResponseEntity.ok(token);
    }

    @RequestMapping(value = "/refresh", method = RequestMethod.GET)
    public ResponseEntity<?> refreshAndGetAuthenticationToken(
            HttpServletRequest request) throws AuthenticationException {
        String token = request.getHeader(tokenHeader);
        String refreshedToken = authService.refresh(token);
        if(refreshedToken == null) {
            return ResponseEntity.badRequest().body(null);
        } else {
            return ResponseEntity.ok(refreshedToken);
        }
    }

    @ApiOperation(value = "登录注册统一接口")
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public RestResult register(@Valid @RequestBody P_User user, HttpServletRequest request) throws AuthenticationException {
//        String name = params.get("name");
//        String qqOpenId = params.get("qqOpenId");
//        String wxUnionId = params.get("wxUnionId");
//        String header = params.get("header");
//        int sex = Integer.parseInt(params.get("sex"));

        String jPushId = request.getHeader(AppConfig.Header.JPUSH_ID);
        User userToAdd = new User(user.getName(),user.getQqOpenId(),user.getWxUnionId(),user.getHeader(), user.getSex());
        userToAdd.setjPushId(jPushId);
        return authService.register(userToAdd);
    }
}
