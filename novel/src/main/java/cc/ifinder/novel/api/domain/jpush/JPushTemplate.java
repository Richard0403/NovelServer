package cc.ifinder.novel.api.domain.jpush;


import cc.ifinder.novel.api.domain.common.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.HashMap;
import java.util.Map;

@Entity
public class JPushTemplate extends BaseEntity {
    @Id
    @GeneratedValue
    private Long id;
    @Column(nullable = false)
    private Integer type;

    private String title;

    private String content;

    private String contentKeys;

    private String url;


    public Map setContentByKeys(String ... keyValues){
        String keys[] = contentKeys.split(",");
        Map map = new HashMap();
        for(int i=0;i<keys.length;i++){
            String key = keys[i];
            map.put(key, keyValues[i]);
            content = content.replace("{"+key+"}", keyValues[i]);
        }
        return map;
    }



    public String getContentKeys() {
        return contentKeys;
    }

    public void setContentKeys(String contentKeys) {
        this.contentKeys = contentKeys;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }


    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
