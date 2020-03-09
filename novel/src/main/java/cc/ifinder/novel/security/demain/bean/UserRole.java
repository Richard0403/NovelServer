package cc.ifinder.novel.security.demain.bean;

import javax.persistence.*;

/** @author: Richard on 2017/12/7 desc: */
@Entity
@Table(name = "user_role")
public class UserRole {
    @Id @GeneratedValue private Long id;
    @Column private String name;

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
}
