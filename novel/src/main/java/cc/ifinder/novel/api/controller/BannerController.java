package cc.ifinder.novel.api.controller;

import cc.ifinder.novel.api.domain.banner.Banner;
import cc.ifinder.novel.api.domain.common.RestResult;
import cc.ifinder.novel.api.service.BannerService;
import cc.ifinder.novel.utils.RestGenerator;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;


/**
 * @author Richard
 * desc 评论
 */
@RestController
@RequestMapping(value = "/banner")
public class BannerController {
    @Autowired
    private BannerService bannerService;

    @ApiOperation(value = "分类获取banner")
    @RequestMapping(value="/queryBanner", method= RequestMethod.POST)
    public RestResult getBanners(@RequestBody Map<String, String> params, HttpServletRequest request){
        int sort = Integer.parseInt(params.get("sort"));
        List<Banner> banners = bannerService.getBanners(sort);
        return RestGenerator.genSuccessResult(banners);
    }

    @ApiOperation(value = "获取所有banner")
    @RequestMapping(value="/queryAll", method= RequestMethod.POST)
    public RestResult getAllBanner(@RequestBody Map<String, String> params, HttpServletRequest request){
        List<Banner> banners = bannerService.getBanners();
        return RestGenerator.genSuccessResult(banners);
    }

}
