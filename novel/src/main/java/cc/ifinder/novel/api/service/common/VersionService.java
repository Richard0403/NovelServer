package cc.ifinder.novel.api.service.common;

import cc.ifinder.novel.api.domain.common.summary.VersionSummary;
import cc.ifinder.novel.api.domain.version.RepositoryVersion;
import cc.ifinder.novel.api.domain.version.Version;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

/**
 * by Richard on 2017/9/10
 * desc:
 */
@Service
public class VersionService {
    @PersistenceContext
    EntityManager entityManager;
    @Autowired
    private RepositoryVersion repositoryVersion;

    public Version getVersion(){
        Version version = repositoryVersion.findTopByOrderByUpdateTimeDesc();
        return version;
    }

    public List<VersionSummary> getVersion(int isForce){
        String sql = "select id, size, version_name from version where is_force=:isForce";
        Query query = entityManager.createNativeQuery(sql, VersionSummary.class);
        query.setParameter("isForce", isForce);
        return query.getResultList();
    }

    public List<VersionSummary> getVersions(int pageSize, int pageNo){
        String sql = "select id, size, version_name from version order by version_code";
        Query query = entityManager.createNativeQuery(sql, VersionSummary.class);
        query.setMaxResults(pageSize);
        query.setFirstResult(pageNo * pageSize);
        return query.getResultList();
    }
}
