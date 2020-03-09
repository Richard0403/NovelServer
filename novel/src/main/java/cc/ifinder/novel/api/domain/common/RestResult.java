package cc.ifinder.novel.api.domain.common;

public class RestResult<T> {
    private int code;
    private String msg;
    private T data;

    private RestResult() {}

    public static <T> RestResult<T> newInstance() {
        return new RestResult<>();
    }



    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "RestResult{" +
                "code=" + code +
                ", message='" + msg + '\'' +
                ", data=" + data +
                '}';
    }
}
