package cc.ifinder.novel.api.service;

import cc.ifinder.novel.api.domain.book.*;
import cc.ifinder.novel.api.domain.common.CommonException;
import cc.ifinder.novel.api.domain.user.User;
import cc.ifinder.novel.constant.AppConfig;
import cc.ifinder.novel.constant.RandomPicture;
import cc.ifinder.novel.security.demain.bean.UserRole;
import com.qiniu.util.StringUtils;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

/** by Richard on 2017/9/10 desc: */
@Service
public class BookService {

    @Autowired private RepositoryBookCategory repositoryBookCategory;
    @Autowired private RepositoryBookInfo repositoryBookInfo;
    @Autowired private RepositoryBookChapter repositoryBookChapter;
    @Autowired private RepositoryBookShelf repositoryBookShelf;
    @Autowired private RepositoryCategoryChangeLog repositoryCategoryChangeLog;

    public enum BookOrder {
        ORDER_HOT,
        ORDER_NEW;
    }

    public List<BookInfo> getCategoryBooks(Long categoryCode, int pageSize, int pageNo) {
        List<BookInfo> bookList = new ArrayList<>();
        Pageable pageable =
                new PageRequest(pageNo, pageSize, Sort.Direction.ASC, "id", "updateTime");
        Page page = repositoryBookInfo.findByBookCategory_Code(categoryCode, pageable);
        bookList.addAll(page.getContent());
        return bookList;
    }

    public List<BookCategory> getCategory() {
        Sort sort = new Sort(Sort.Direction.DESC, "updateTime");
        return repositoryBookCategory.findAll(sort);
    }

    public BookCategory getCategory(Long code) {
        return repositoryBookCategory.findTopByCode(code);
    }

    public List<BookChapterInfo> getChapterList(Long bookId) {
        return repositoryBookChapter.findByBookInfo_IdOrderByChapterNo(bookId);
    }

    public BookChapterInfo getChapter(Long chapterId) {
        return repositoryBookChapter.getById(chapterId);
    }

    public List<BookInfo> getBooks(int pageSize, int pageNo, BookOrder order, String categoryStr) {
        List<String> orderParam = new ArrayList<>();
        switch (order) {
            case ORDER_HOT:
                orderParam.add("readTimes");
                break;
            case ORDER_NEW:
                orderParam.add("updateTime");
                break;
        }
        List<BookInfo> bookList = new ArrayList<>();
        String[] strings = new String[orderParam.size()];
        Pageable pageable =
                new PageRequest(pageNo, pageSize, Sort.Direction.DESC, orderParam.toArray(strings));
        if (StringUtils.isNullOrEmpty(categoryStr)) {
            Page page = repositoryBookInfo.findAll(pageable);
            bookList.addAll(page.getContent());
        } else {
            Long categoryCode = Long.parseLong(categoryStr);
            Page page = repositoryBookInfo.findByBookCategory_Code(categoryCode, pageable);
            bookList.addAll(page.getContent());
        }
        return bookList;
    }

    public BookInfo getBookInfo(Long bookId) {
        BookInfo bookInfo = repositoryBookInfo.findById(bookId).get();
        bookInfo.setReadTimes(bookInfo.getReadTimes() + 1);
        return repositoryBookInfo.save(bookInfo);
    }

    public List<BookInfo> searchBook(int pageNo, int pageSize, String keywords) {
        List<BookInfo> bookList = new ArrayList<>();
        Pageable pageable = new PageRequest(pageNo, pageSize);
        Page page =
                repositoryBookInfo.findByNameLikeOrAuthorLike(
                        "%" + keywords + "%", "%" + keywords + "%", pageable);
        bookList.addAll(page.getContent());
        return bookList;
    }

    public BookInfo addOrUpdateBook(
            String name,
            String author,
            String source,
            String summary,
            String cover,
            Long categoryCode) {
        BookInfo bookInfo = repositoryBookInfo.findTopByNameAndAuthor(name, author);
        if (bookInfo == null) {
            if (StringUtils.isNullOrEmpty(cover)) {
                cover = RandomPicture.DEFAULT_COVER;
            }
            BookCategory category = repositoryBookCategory.findTopByCode(categoryCode);
            bookInfo = new BookInfo(category, name, author, summary, cover);
            bookInfo.setSource(source);
        } else { // 书籍就只更新来源，封面
            bookInfo.setSource(source);
            bookInfo.setCover(cover);
        }
        BookInfo newbook = repositoryBookInfo.save(bookInfo);
        return newbook;
    }

    public void addOrUpdateChapter(Long book_id, int chapter_no, String title, String content)
            throws CommonException {
        BookInfo bookInfo = repositoryBookInfo.findById(book_id).get();
        if (bookInfo != null) {
            BookChapterInfo bookChapterInfo =
                    repositoryBookChapter.findTopByBookInfo_IdAndChapterNo(book_id, chapter_no);
            boolean isNew = false; // 是否新添加的章节
            if (bookChapterInfo == null) {
                isNew = true;
                bookChapterInfo =
                        new BookChapterInfo(bookInfo, chapter_no, title, content.length(), content);
            } else {
                bookChapterInfo.setBookInfo(bookInfo);
                bookChapterInfo.setChapterNo(chapter_no);
                bookChapterInfo.setContent(content);
                bookChapterInfo.setTitle(title);
                bookChapterInfo.setWordNum(content.length());
            }
            repositoryBookChapter.save(bookChapterInfo);
            if (isNew) {
                bookInfo.setChapter_num(bookInfo.getChapter_num() + 1);
                repositoryBookInfo.save(bookInfo);
            }
        } else {
            throw new CommonException("不存在该书籍");
        }
    }

    public BookInfo changBookCategory(User user, Long bookId, Long newCategoryCode)
            throws CommonException {
        BookInfo bookInfo = repositoryBookInfo.findById(bookId).get();
        BookCategory category = repositoryBookCategory.findTopByCode(newCategoryCode);

        if (bookInfo != null && category != null) {

            boolean isAdmin = false;
            for (UserRole role : user.getUserRoles()) {
                if (AppConfig.Role.USER_RULE_ADMIN.equals(role.getName())) {
                    isAdmin = true;
                }
            }
            if (isAdmin) {
                return saveCategory(bookInfo, category, user);
            } else {
                Long before = System.currentTimeMillis() - 24 * 60 * 60 * 1000;
                List<CategoryChangeLog> logs =
                        repositoryCategoryChangeLog
                                .getCategoryChangeLogByUserIdAndCreateTimeAfterOrderByCreateTime(
                                        user.getId(), new Date(before));
                if (logs.size() >= 5) { // >=5不在允许修改
                    throw new CommonException("普通用户每天最多修正5本书的分类");
                } else {
                    return saveCategory(bookInfo, category, user);
                }
            }
        } else {
            throw new CommonException("分类或书籍不存在");
        }
    }

    /**
     * 更改分类分类
     *
     * @param bookInfo
     * @param category
     * @return
     */
    private BookInfo saveCategory(BookInfo bookInfo, BookCategory category, User user) {
        bookInfo.setBookCategory(category);
        // 保存修改记录
        CategoryChangeLog changeLog =
                new CategoryChangeLog(
                        bookInfo.getId(),
                        bookInfo.getName(),
                        user.getId(),
                        user.getName(),
                        category.getCode(),
                        category.getName());
        repositoryCategoryChangeLog.save(changeLog);

        return repositoryBookInfo.save(bookInfo);
    }

    public BookInfo searchByNameAuthor(String name, String author) {
        return repositoryBookInfo.findTopByNameAndAuthor(name, author);
    }
}
