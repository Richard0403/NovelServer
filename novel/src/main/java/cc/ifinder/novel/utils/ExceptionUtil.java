package cc.ifinder.novel.utils;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * by Richard on 2017/8/29
 * desc: 异常输出
 */
public class ExceptionUtil {
    public static String getErrorInfo(Exception e) {
        try {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            return e.getMessage()+"\r\n" + sw.toString() + "\r\n";
        } catch (Exception e2) {
            return "bad getErrorInfoFromException";
        }
    }
}
