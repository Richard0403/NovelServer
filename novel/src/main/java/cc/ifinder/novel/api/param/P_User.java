package cc.ifinder.novel.api.param;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;

public class P_User {

    @NotEmpty(message = "名字不能为空")
    private String name;
    @NotEmpty(message = "头像不能为空")
    private String header;

    private String qqOpenId;

    private String wxUnionId;
    @Min(value = 0,message = "性别值区间0-2")@Max(value = 2,message = "性别值区间0-2")
    private int sex;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getQqOpenId() {
        return qqOpenId;
    }

    public void setQqOpenId(String qqOpenId) {
        this.qqOpenId = qqOpenId;
    }

    public String getWxUnionId() {
        return wxUnionId;
    }

    public void setWxUnionId(String wxUnionId) {
        this.wxUnionId = wxUnionId;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }
}
