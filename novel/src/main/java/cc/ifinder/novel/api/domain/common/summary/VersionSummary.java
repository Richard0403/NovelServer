package cc.ifinder.novel.api.domain.common.summary;

import javax.persistence.*;

@Entity
@SqlResultSetMapping(
        name = "versionMapping",
        entities =
                @EntityResult(
                        entityClass = VersionSummary.class,
                        fields = {
                            @FieldResult(name = "id", column = "id"),
                            @FieldResult(name = "size", column = "size"),
                            @FieldResult(name = "versionName", column = "version_name")
                        }))
// @NamedStoredProcedureQueries({
//        @NamedStoredProcedureQuery(
//                name = "getContactsLikeName",
//                procedureName = "proc_get_contacts_like_name",
//                resultClasses = { VersionSummary.class },
//                parameters = {
//                        @StoredProcedureParameter(
//                                mode = ParameterMode.IN,
//                                name = "name",
//                                type = String.class)
//                }
//        )
// })
public class VersionSummary {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private double size;
    private String versionName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public double getSize() {
        return size;
    }

    public void setSize(double size) {
        this.size = size;
    }

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }
}
