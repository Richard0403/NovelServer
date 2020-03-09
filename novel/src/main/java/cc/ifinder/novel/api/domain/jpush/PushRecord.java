package cc.ifinder.novel.api.domain.jpush;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class PushRecord {
    @Id
    @GeneratedValue
    private Long id;


}
