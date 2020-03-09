package cc.ifinder.novel.api.domain.user;

import cc.ifinder.novel.api.domain.common.BaseEntity;
import cc.ifinder.novel.security.demain.bean.UserRole;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.*;

/** 用户信息 */
@Entity
@Table(name = "user")
public class User extends BaseEntity implements Serializable {
    @Id @GeneratedValue private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false, unique = true)
    @JsonIgnore
    private String uniqueName;

    @Column(nullable = false, unique = true)
    @JsonIgnore
    private String qqOpenId;

    @JsonIgnore private String wxUnionId;

    private Integer age;

    @Column(columnDefinition = "int COMMENT '0--female 1--male 2--unKnown'")
    private int sex = 0;

    private String header;
    @JsonIgnore private String password;
    @JsonIgnore private Date lastPasswordResetDate;
    @JsonIgnore private String token;
    @JsonIgnore private String jPushId;

    @Column(nullable = false)
    private String inviteCode;

    private String fromInviteCode;

    @ManyToMany(
            cascade = {CascadeType.REFRESH},
            fetch = FetchType.EAGER)
    @JsonIgnore
    @JoinTable(
            name = "extra_role_user",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private List<UserRole> userRoles;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Date getLastPasswordResetDate() {
        return lastPasswordResetDate;
    }

    public void setLastPasswordResetDate(Date lastPasswordResetDate) {
        this.lastPasswordResetDate = lastPasswordResetDate;
    }

    public List<UserRole> getUserRoles() {
        return userRoles;
    }

    public void setUserRoles(List<UserRole> userRoles) {
        this.userRoles = userRoles;
    }

    User() {}

    public User(String name, String qqOpenId, String wxUnionId, String header, int sex) {
        this.name = name;
        this.qqOpenId = qqOpenId;
        this.wxUnionId = wxUnionId;
        this.header = header;
        this.sex = sex;
    }

    public String getInviteCode() {
        return inviteCode;
    }

    public void setInviteCode(String inviteCode) {
        this.inviteCode = inviteCode;
    }

    public String getFromInviteCode() {
        return fromInviteCode;
    }

    public void setFromInviteCode(String fromInviteCode) {
        this.fromInviteCode = fromInviteCode;
    }

    public String getjPushId() {
        return jPushId;
    }

    public void setjPushId(String jPushId) {
        this.jPushId = jPushId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUniqueName() {
        return uniqueName;
    }

    public void setUniqueName(String uniqueName) {
        this.uniqueName = uniqueName;
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

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public boolean equals(Object obj) {
        if (getId() == (((User) obj).getId())) {
            return true;
        } else {
            return false;
        }
    }
}
