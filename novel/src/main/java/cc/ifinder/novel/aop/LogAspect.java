package cc.ifinder.novel.aop;

import java.util.Arrays;
import javax.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/** 日志切面 */
@Aspect
@Component
@Order(1)
public class LogAspect {

    private static Logger logger = LogManager.getLogger("record");

    @Pointcut("execution(public * cc.ifinder.novel.api.controller..*.*(..))")
    public void webLog() {}

    @Before("webLog()")
    public void deBefore(JoinPoint joinPoint) throws Throwable {
        // 接收到请求，记录请求内容
        ServletRequestAttributes attributes =
                (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();

        // 记录下请求内容
        logger.debug("URL : " + request.getRequestURL().toString());
        logger.debug("IP : " + request.getRemoteAddr());
        logger.debug(
                "CLASS_METHOD : "
                        + joinPoint.getSignature().getDeclaringTypeName()
                        + "."
                        + joinPoint.getSignature().getName());
        logger.debug("ARGS : " + Arrays.toString(joinPoint.getArgs()));
    }

    @AfterReturning(returning = "ret", pointcut = "webLog()")
    public void doAfterReturning(Object ret) throws Throwable {
        // 处理完请求，返回内容

        logger.debug("方法的返回值 : " + (ret == null ? "返回值为空" : ret.toString()));
    }

    // 后置异常通知
    @AfterThrowing("webLog()")
    public void throwss(JoinPoint jp) {
        logger.debug("方法异常时执行.....");
    }

    // 后置最终通知,final增强，不管是抛出异常或者正常退出都会执行
    @After("webLog()")
    public void after(JoinPoint jp) {
        logger.debug("方法最后执行.....");
    }

    // 环绕通知,环绕增强，相当于MethodInterceptor
    @Around("webLog()")
    public Object arround(ProceedingJoinPoint pjp) throws Throwable {
        Long start = System.currentTimeMillis();
        Object o = pjp.proceed();
        //            logger.debug("方法环绕proceed，结果是 :" + o);
        Long end = System.currentTimeMillis();
        logger.debug("耗时: " + (end - start) + " ms!");
        return o;
    }
}
