package cc.ifinder.novel.security;

import cc.ifinder.novel.api.domain.common.RestResult;
import cc.ifinder.novel.constant.ErrorCode;
import cc.ifinder.novel.utils.RestGenerator;
import com.google.gson.Gson;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint, Serializable {

    private static final Long serialVersionUID = -8970718410437077606L;

    @Override
    public void commence(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException authException)
            throws IOException {
        // This is invoked when user tries to access a secured REST resource without supplying any
        // credentials
        // We should just send a 401 Unauthorized response because there is no 'login page' to
        // redirect to
        //        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
        RestResult result =
                RestGenerator.genResult(ErrorCode.INT_ERROR_TOKEN, null, ErrorCode.ERROR_TOKEN);
        Gson gson = new Gson();
        String userJson = gson.toJson(result);
        OutputStream out = response.getOutputStream();
        out.write(userJson.getBytes("UTF-8"));
        out.flush();
    }
}
