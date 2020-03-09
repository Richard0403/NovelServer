package cc.ifinder.novel;

import cc.ifinder.novel.api.domain.common.CommonException;
import cc.ifinder.novel.api.domain.common.RestResult;
import cc.ifinder.novel.constant.ErrorCode;
import cc.ifinder.novel.utils.ExceptionUtil;
import cc.ifinder.novel.utils.RestGenerator;
import org.hibernate.service.spi.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@ResponseBody
@ControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);

//    /**
//     * 系统异常处理，比如：404,500
//     * @param req
//     * @param e
//     * @return
//     * @throws Exception
//     */
    @ExceptionHandler(value = CommonException.class)
    @ResponseBody
    public <T> RestResult<T> defaultErrorHandler(HttpServletRequest req, CommonException e) {
        LOGGER.error(ExceptionUtil.getErrorInfo(e));
        e.printStackTrace();
        return RestGenerator.genErrorResult(e.getMessage());
    }

    @ExceptionHandler
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public  <T> RestResult<T> runtimeExceptionHandler(Exception e) {
        LOGGER.error("---------> huge error!", e);
        e.printStackTrace();
        return RestGenerator.genErrorResult(ErrorCode.ERROR_SERVER);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public  <T> RestResult<T> illegalParamsExceptionHandler(MethodArgumentNotValidException e) {
        LOGGER.error("---------> invalid request!", e);
        e.printStackTrace();
        List<ObjectError> objectErrors = e.getBindingResult().getAllErrors();
        if(!CollectionUtils.isEmpty(objectErrors)) {
            StringBuilder msgBuilder = new StringBuilder();
            for (ObjectError objectError : objectErrors) {
                msgBuilder.append(objectError.getDefaultMessage()).append(",");
            }
            String errorMessage = msgBuilder.toString();
            if (errorMessage.length() > 1) {
                errorMessage = errorMessage.substring(0, errorMessage.length() - 1);
            }
            return RestGenerator.genErrorResult(errorMessage);
        }
        return RestGenerator.genErrorResult(e.getMessage());
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public  <T> RestResult<T> accessDeniedException(AccessDeniedException e) {
        LOGGER.error("---------> access denied!", e);
        e.printStackTrace();
        return RestGenerator.genErrorResult(ErrorCode.ERROR_ACCESS);
    }

    @ExceptionHandler
    @ResponseBody
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public  <T> RestResult<T> notFoundExceptionHandler(NoHandlerFoundException e) {
        LOGGER.error("---------> invalid request!", e);
        return RestGenerator.genErrorResult(ErrorCode.ERROR_NOT_FOUND);
    }

    /**
     * 操作数据库出现异常:名称重复，外键关联
     */
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(DataIntegrityViolationException.class)
    public  <T> RestResult<T> handleException(DataIntegrityViolationException e) {
        LOGGER.error("操作数据库出现异常:", e);
        e.printStackTrace();
        return  RestGenerator.genErrorResult("操作数据库出现异常：字段重复、有外键关联等");
    }

    /**
     * 500 - Internal Server Error
     */
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(ServiceException.class)
    public <T> RestResult<T> handleServiceException(ServiceException e) {
        LOGGER.error("业务逻辑异常", e);
        e.printStackTrace();
        return RestGenerator.genErrorResult("业务逻辑异常：" + e.getMessage());
    }

}
