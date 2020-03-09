package cc.ifinder.novel.utils;

import cc.ifinder.novel.api.domain.common.RestResult;
import cc.ifinder.novel.constant.ErrorCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RestGenerator {
    private static final Logger LOGGER = LoggerFactory.getLogger(RestGenerator.class);

    /**
     * normal
     * @param code
     * @param data
     * @param message
     * @param <T>
     * @return
     */
    public static <T> RestResult<T> genResult(int code, T data, String message) {
        RestResult<T> result = RestResult.newInstance();
        result.setCode(code);
        result.setData(data);
        result.setMsg(message);
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("generate rest result:{}", result);
        }
        return result;
    }


    /**
     * success
     * @param msg
     * @return
     */
    public static <T> RestResult<T> genSuccessResult(T data, String msg) {
        return genResult(ErrorCode.OK, data, msg);
    }

    public static <T> RestResult<T> genSuccessResult(T data) {
        return genResult(ErrorCode.OK, data, ErrorCode.ERROR_OK);
    }
    public static <T> RestResult<T> genSuccessResult() {
        return genResult(ErrorCode.OK, null, ErrorCode.ERROR_OK);
    }

    /**
     * error message
     * @param message error message
     * @param <T>
     * @return
     */
    public static <T> RestResult<T> genErrorResult(String message) {

        return genResult(ErrorCode.ERROR, null, message);
    }
}
