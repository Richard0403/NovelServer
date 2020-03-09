package cc.ifinder.novel.api.service;

import cc.ifinder.novel.api.domain.book.BookInfo;
import cc.ifinder.novel.api.domain.book.BookShelf;
import cc.ifinder.novel.api.domain.book.RepositoryBookShelf;
import cc.ifinder.novel.api.domain.user.User;
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
public class BookShelfService {

    @Autowired private RepositoryBookShelf repositoryBookShelf;
    @Autowired private BookService bookService;

    public List<BookShelf> getBookShelves(int pageNo, int pageSize, User user) {
        List<BookShelf> bookList = new ArrayList<>();
        Pageable pageable = new PageRequest(pageNo, pageSize, Sort.Direction.DESC, "updateTime");
        Page page = repositoryBookShelf.findByUser_Id(user.getId(), pageable);
        bookList.addAll(page.getContent());
        return bookList;
    }

    public List<BookShelf> getBookShelves(int pageNo, int pageSize) {
        List<BookShelf> bookList = new ArrayList<>();
        Pageable pageable = new PageRequest(pageNo, pageSize, Sort.Direction.DESC, "updateTime");
        Page page = repositoryBookShelf.findAll(pageable);
        bookList.addAll(page.getContent());
        return bookList;
    }

    public BookShelf addShelf(Long bookId, User user) {
        BookInfo bookInfo = bookService.getBookInfo(bookId);
        BookShelf bookShelf =
                repositoryBookShelf.findFirstByBookInfo_IdAndUser_Id(bookId, user.getId());
        if (bookShelf == null) {
            bookShelf = new BookShelf();
            bookShelf.setBookInfo(bookInfo);
            bookShelf.setUser(user);
            bookShelf.setLastReadTime(new Date());
            repositoryBookShelf.save(bookShelf);
        }
        return bookShelf;
    }

    public void removeShelf(User user, String... shelfId) {
        for (String idStr : shelfId) {
            BookShelf bookShelf = repositoryBookShelf.findById(Long.parseLong(idStr)).get();
            if (bookShelf.getUser().equals(user)) {
                repositoryBookShelf.delete(bookShelf);
            }
        }
    }

    public void updateBookShelf(Long shelfId, User user, int chapterPos, int pagePos) {
        BookShelf bookShelf = repositoryBookShelf.findById(shelfId).get();
        if (bookShelf.getUser().equals(user)) {
            if (chapterPos != 0) bookShelf.setChapterPos(chapterPos);
            if (pagePos != 0) bookShelf.setPagePos(pagePos);
            bookShelf.setLastReadTime(new Date());
            repositoryBookShelf.save(bookShelf);
        }
    }
}
