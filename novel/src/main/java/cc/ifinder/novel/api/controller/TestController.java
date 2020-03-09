package cc.ifinder.novel.api.controller;

/**
 * by Richard on 2017/9/24
 * desc:
 */

import cc.ifinder.novel.api.domain.common.RestResult;
import cc.ifinder.novel.api.domain.version.RepositoryVersion;
import cc.ifinder.novel.api.domain.version.Version;
import cc.ifinder.novel.constant.ErrorCode;
import cc.ifinder.novel.utils.RestGenerator;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(value = "/test")
public class TestController {
    @Autowired
    private RepositoryVersion versionRep;

    @ApiOperation(value = "测试")
    @RequestMapping(value="/getVersion", method= RequestMethod.GET)
    public RestResult getVersion(HttpServletRequest request){
        Version version = versionRep.findTopByOrderByUpdateTimeDesc();
        return RestGenerator.genResult(ErrorCode.OK, version, "获取成功");
    }

    @ApiOperation(value = "测试")
    @RequestMapping(value="/addVersion", method= RequestMethod.GET)
    public RestResult addVersion(@RequestParam("name") String name, HttpServletRequest request){
        Version version = new Version();
        version.setVersionName(name);
        versionRep.save(version);
        version = versionRep.findByVersionName(name);
        return RestGenerator.genResult(ErrorCode.OK, version, "添加成功");
    }
}
