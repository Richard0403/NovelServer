package cc.ifinder.novel.api.service;

import cc.ifinder.novel.api.domain.banner.Banner;
import cc.ifinder.novel.api.domain.banner.RepositoryBanner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * by Richard on 2017/9/10
 * desc:
 */
@Service
public class BannerService {

    @Autowired
    private RepositoryBanner repositoryBanner;


    public List<Banner> getBanners(int sort){
        if(sort == 0){
            return getBanners();
        }
        return repositoryBanner.findAllBySort(sort);
    }

    public List<Banner> getBanners(){
        return repositoryBanner.findAll();
    }

}
