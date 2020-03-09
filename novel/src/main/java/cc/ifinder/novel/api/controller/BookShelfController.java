package cc.ifinder.novel.api.controller;

import cc.ifinder.novel.api.domain.book.BookShelf;
import cc.ifinder.novel.api.domain.common.RestResult;
import cc.ifinder.novel.api.domain.user.User;
import cc.ifinder.novel.api.service.BookShelfService;
import cc.ifinder.novel.utils.RestGenerator;
import cc.ifinder.novel.utils.TokenUtil;
import io.swagger.annotations.ApiOperation;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/** @author Richard desc 书架 */
@RestController
@RequestMapping(value = "/shelf")
public class BookShelfController {
    @Autowired private TokenUtil tokenUtil;
    @Autowired private BookShelfService shelfService;

    @ApiOperation(value = "获取书架书籍")
    @RequestMapping(value = "/getShelfBooks", method = RequestMethod.POST)
    public RestResult getShelfBooks(
            @RequestBody Map<String, String> params, HttpServletRequest request) {
        User user = tokenUtil.getUserIdFromHttpReq(request);
        int pageSize = Integer.parseInt(params.get("pageSize"));
        int pageNo = Integer.parseInt(params.get("pageNo"));
        List<BookShelf> shelfBooks = shelfService.getBookShelves(pageNo, pageSize, user);
        return RestGenerator.genSuccessResult(shelfBooks);
    }

    @ApiOperation(value = "获取所有书架书籍")
    @RequestMapping(value = "/getAllShelfBooks", method = RequestMethod.POST)
    public RestResult getAllShelfBooks(@RequestBody Map<String, String> params) {
        int pageSize = Integer.parseInt(params.get("pageSize"));
        int pageNo = Integer.parseInt(params.get("pageNo"));
        List<BookShelf> shelfBooks = shelfService.getBookShelves(pageNo, pageSize);
        return RestGenerator.genSuccessResult(shelfBooks);
    }

    @ApiOperation(value = "添加书架")
    @RequestMapping(value = "/addBookShelf", method = RequestMethod.POST)
    public RestResult addBookShelf(
            @RequestBody Map<String, String> params, HttpServletRequest request) {
        User user = tokenUtil.getUserIdFromHttpReq(request);
        Long bookId = Long.parseLong(params.get("bookId"));
        BookShelf bookShelf = shelfService.addShelf(bookId, user);
        if (bookShelf != null) {
            return RestGenerator.genSuccessResult(bookShelf);
        } else {
            return RestGenerator.genErrorResult("添加失败");
        }
    }

    @ApiOperation(value = "移除书架")
    @RequestMapping(value = "/removeBookShelf", method = RequestMethod.POST)
    public RestResult removeBookShelf(
            @RequestBody Map<String, String> params, HttpServletRequest request) {
        User user = tokenUtil.getUserIdFromHttpReq(request);
        String shelfIds = String.valueOf(params.get("shelfIds"));
        String shelfId[] = shelfIds.split(",");
        shelfService.removeShelf(user, shelfId);
        return RestGenerator.genSuccessResult();
    }

    @ApiOperation(value = "更新书架")
    @RequestMapping(value = "/updateBookShelf", method = RequestMethod.POST)
    public RestResult updateReadTime(
            @RequestBody Map<String, String> params, HttpServletRequest request) {
        User user = tokenUtil.getUserIdFromHttpReq(request);
        Long shelfId = Long.parseLong(params.get("shelfId"));
        int chapterPos = Integer.parseInt(params.get("chapterPos"));
        int pagePos = Integer.parseInt(params.get("pagePos"));
        shelfService.updateBookShelf(shelfId, user, chapterPos, pagePos);
        return RestGenerator.genSuccessResult();
    }
}
