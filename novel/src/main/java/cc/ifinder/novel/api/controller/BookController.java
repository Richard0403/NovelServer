package cc.ifinder.novel.api.controller;

import cc.ifinder.novel.api.domain.book.BookCategory;
import cc.ifinder.novel.api.domain.book.BookChapterInfo;
import cc.ifinder.novel.api.domain.book.BookInfo;
import cc.ifinder.novel.api.domain.common.CommonException;
import cc.ifinder.novel.api.domain.common.RestResult;
import cc.ifinder.novel.api.domain.user.User;
import cc.ifinder.novel.api.service.BookService;
import cc.ifinder.novel.utils.RestGenerator;
import cc.ifinder.novel.utils.TokenUtil;
import com.qiniu.util.StringUtils;
import io.swagger.annotations.ApiOperation;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/** @author Richard desc 书籍 */
@RestController
@RequestMapping(value = "/book")
public class BookController {
    @Autowired private TokenUtil tokenUtil;
    @Autowired private BookService bookService;

    @ApiOperation(value = "获取分类")
    @RequestMapping(value = "/getCategory", method = RequestMethod.POST)
    public RestResult getCategory(
            @RequestBody Map<String, String> params, HttpServletRequest request) {
        List<BookCategory> categories = bookService.getCategory();
        return RestGenerator.genSuccessResult(categories);
    }

    @ApiOperation(value = "获取单个分类信息")
    @RequestMapping(value = "/getSingleCategory", method = RequestMethod.POST)
    public RestResult getSingleCategory(
            @RequestBody Map<String, String> params, HttpServletRequest request) {
        Long code = Long.valueOf(params.get("category"));
        BookCategory category = bookService.getCategory(code);
        return RestGenerator.genSuccessResult(category);
    }

    @ApiOperation(value = "获取分类下的书籍")
    @RequestMapping(value = "/getCategoryBooks", method = RequestMethod.POST)
    public RestResult getCategoryBooks(@RequestBody Map<String, String> params) {
        Long categoryCode = Long.parseLong(params.get("categoryCode"));
        int pageSize = Integer.parseInt(params.get("pageSize"));
        int pageNo = Integer.parseInt(params.get("pageNo"));
        List<BookInfo> bookInfoList = bookService.getCategoryBooks(categoryCode, pageSize, pageNo);
        return RestGenerator.genSuccessResult(bookInfoList);
    }

    @ApiOperation(value = "获取书籍的目录")
    @RequestMapping(value = "/getChapterList", method = RequestMethod.POST)
    public RestResult getChapterList(@RequestBody Map<String, String> params) {
        Long bookId = Long.parseLong(params.get("bookId"));
        List<BookChapterInfo> chapterInfoList = bookService.getChapterList(bookId);
        return RestGenerator.genSuccessResult(chapterInfoList);
    }

    @ApiOperation(value = "获取目录内容")
    @RequestMapping(value = "/getChapterContent", method = RequestMethod.POST)
    public RestResult getChapterContent(@RequestBody Map<String, String> params) {
        Long chapterId = Long.parseLong(params.get("chapterId"));
        BookChapterInfo chapterInfo = bookService.getChapter(chapterId);
        return RestGenerator.genSuccessResult(chapterInfo);
    }

    @ApiOperation(value = "获取热门书籍")
    @RequestMapping(value = "/getHotBooks", method = RequestMethod.POST)
    public RestResult getHotBooks(@RequestBody Map<String, String> params) {
        int pageSize = Integer.parseInt(params.get("pageSize"));
        int pageNo = Integer.parseInt(params.get("pageNo"));
        String categoryStr = params.get("category");

        List<BookInfo> bookInfoList =
                bookService.getBooks(
                        pageSize, pageNo, BookService.BookOrder.ORDER_HOT, categoryStr);
        return RestGenerator.genSuccessResult(bookInfoList);
    }

    @ApiOperation(value = "获取最新书籍")
    @RequestMapping(value = "/getNewBooks", method = RequestMethod.POST)
    public RestResult getNewBooks(@RequestBody Map<String, String> params) {
        int pageSize = Integer.parseInt(params.get("pageSize"));
        int pageNo = Integer.parseInt(params.get("pageNo"));
        String categoryStr = params.get("category");

        List<BookInfo> bookInfoList =
                bookService.getBooks(
                        pageSize, pageNo, BookService.BookOrder.ORDER_NEW, categoryStr);
        return RestGenerator.genSuccessResult(bookInfoList);
    }

    @ApiOperation(value = "书籍详情")
    @RequestMapping(value = "/getBookDetail", method = RequestMethod.POST)
    public RestResult getBookDetail(@RequestBody Map<String, String> params) {
        long bookId = Integer.parseInt(params.get("bookId"));
        BookInfo bookInfo = bookService.getBookInfo(bookId);
        return RestGenerator.genSuccessResult(bookInfo);
    }

    @ApiOperation(value = "书籍搜索")
    @RequestMapping(value = "/searchBook", method = RequestMethod.POST)
    public RestResult searchBook(@RequestBody Map<String, String> params) {
        String keywords = params.get("keywords");
        int pageSize = Integer.parseInt(params.get("pageSize"));
        int pageNo = Integer.parseInt(params.get("pageNo"));
        List<BookInfo> bookList = bookService.searchBook(pageNo, pageSize, keywords);
        return RestGenerator.genSuccessResult(bookList);
    }

    @ApiOperation(value = "修改书籍分类")
    @RequestMapping(value = "/changeBookCategory", method = RequestMethod.POST)
    public RestResult changeBookCategory(
            @RequestBody Map<String, String> params, HttpServletRequest request)
            throws CommonException {
        Long bookId = Long.valueOf(params.get("bookId"));
        Long categoryCode = Long.valueOf(params.get("category"));
        User user = tokenUtil.getUserIdFromHttpReq(request);
        BookInfo bookInfo = bookService.changBookCategory(user, bookId, categoryCode);
        return RestGenerator.genSuccessResult(bookInfo);
    }

    @Secured({"ROLE_ADMIN"}) // 此方法只允许 RO
    @ApiOperation(value = "添加或者覆盖书籍")
    @RequestMapping(value = "/addOrUpdateBook", method = RequestMethod.POST)
    public RestResult addOrUpdateBook(@RequestBody Map<String, String> params) {
        String name = params.get("name");
        String author = params.get("author");
        String source = params.get("source");
        String summary = params.get("summary");
        String cover = params.get("cover");
        Long category = Long.valueOf(params.get("category"));

        BookInfo bookInfo =
                bookService.addOrUpdateBook(name, author, source, summary, cover, category);
        return RestGenerator.genSuccessResult(bookInfo);
    }

    @Secured({"ROLE_ADMIN"})
    @ApiOperation(value = "添加或者覆盖章节")
    @RequestMapping(value = "/addOrUpdateChapter", method = RequestMethod.POST)
    public RestResult addOrUpdateChapter(@RequestBody Map<String, String> params)
            throws CommonException {
        Long book_id = Long.valueOf(params.get("book_id"));
        int chapter_no = Integer.valueOf(params.get("chapter_no"));
        String title = params.get("title");
        String content = params.get("content");
        if (StringUtils.isNullOrEmpty(content)) {
            return RestGenerator.genErrorResult("章节内容为空");
        }
        bookService.addOrUpdateChapter(book_id, chapter_no, title, content);
        return RestGenerator.genSuccessResult();
    }

    @Secured({"ROLE_ADMIN"})
    @ApiOperation(value = "书名作者查询书籍")
    @RequestMapping(value = "/searchByNameAuthor", method = RequestMethod.POST)
    public RestResult searchByNameAuthor(@RequestBody Map<String, String> params) {
        String name = params.get("name");
        String author = params.get("author");

        BookInfo bookInfo = bookService.searchByNameAuthor(name, author);
        return RestGenerator.genSuccessResult(bookInfo);
    }
}
