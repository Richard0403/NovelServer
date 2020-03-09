package cc.ifinder.novel.security.sign;

import cc.ifinder.novel.api.domain.common.CommonException;
import cc.ifinder.novel.api.domain.user.repository.RepositoryUser;
import cc.ifinder.novel.constant.ErrorCode;
import cc.ifinder.novel.utils.DesUtil;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * by Richard on 2017/8/31
 * desc:网络请求拦截
 */
public class ReqInterceptor implements HandlerInterceptor {
    private Logger logger =  LoggerFactory.getLogger(this.getClass());
    @Autowired
    RepositoryUser repositoryUser;


    @Value("${sign.open}")
    private boolean isOpenSign;

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
        logger.debug(">>>ReqInterceptor>>>>>>>在请求之前");
        if(isOpenSign){//系统配置是否验签
            ServletRequestWrapper mWrapper = new ServletRequestWrapper(httpServletRequest);

            try {
                String body = mWrapper.getBody();
                String headerSign = httpServletRequest.getHeader("sign");
                String decodeSign = DesUtil.decode(headerSign);

                Map<String, Object> signMap = new Gson().fromJson(decodeSign, HashMap.class);
                String decodeParam =  DesUtil.decode(String.valueOf(signMap.get("request")));
                Map<String, Object> paramMap = new Gson().fromJson(decodeParam, HashMap.class);
                //TODO 签名
                if(body.equals(decodeParam)){
                    logger.debug("签名成功");
                }else{
                    logger.debug("签名失败");
                    throw new CommonException(ErrorCode.ERROR_SIGN);
                }
            }catch (Exception e){
                throw new CommonException(ErrorCode.ERROR_SIGN);
            }
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {
        logger.debug(">>>ReqInterceptor>>>>>>>在请求进行中");
    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {
        logger.debug(">>>ReqInterceptor>>>>>>>在请求结束");
    }
}
