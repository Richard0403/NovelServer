package cc.ifinder.novel.api.domain.version;

import cc.ifinder.novel.api.domain.common.BaseEntity;
import javax.persistence.*;

/** by Richard on 2017/9/20 desc: */
@Entity
@Table
public class Version extends BaseEntity {
    @Id @GeneratedValue private Long id;

    @Column(columnDefinition = "int COMMENT '0.不提示更新，1.提示更新，2.强制更新'")
    private int isForce;

    private double size;
    private String updateContent;
    private String url;
    private int versionCode;
    private String versionName;

    public Version() {}

    public Version(
            int isForce,
            double size,
            String updateContent,
            String url,
            int versionCode,
            String versionName) {
        this.isForce = isForce;
        this.size = size;
        this.updateContent = updateContent;
        this.url = url;
        this.versionCode = versionCode;
        this.versionName = versionName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getIsForce() {
        return isForce;
    }

    public void setIsForce(int isForce) {
        this.isForce = isForce;
    }

    public double getSize() {
        return size;
    }

    public void setSize(double size) {
        this.size = size;
    }

    public String getUpdateContent() {
        return updateContent;
    }

    public void setUpdateContent(String updateContent) {
        this.updateContent = updateContent;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(int versionCode) {
        this.versionCode = versionCode;
    }

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }
}
