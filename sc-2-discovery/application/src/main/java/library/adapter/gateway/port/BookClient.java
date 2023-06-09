package library.adapter.gateway.port;

import library.dto.Book;

import org.springframework.cloud.openfeign.FeignClient;
//import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@FeignClient(name = "library-book-service")
//@FeignClient(name = "library-book-service", fallback = BookClient.BookClientFallback.class)
public interface BookClient {
    @ToString
    @RequiredArgsConstructor(staticName = "of")
    public class BooksRequest {
        public @NonNull Long readerId;
        public @NonNull Long bookIds[];
    }

    @GetMapping("/books")
    public List<Book> getBooks(
        @RequestParam("page") Optional<Integer> pageNum,
        @RequestParam("size") Optional<Integer> pageSize,
        @RequestParam("reader") Optional<Long> readerId);

    @PostMapping("/books/commands/load")
    public List<Book> loadDatabase(@RequestParam Optional<Integer> count);

    @PostMapping("/books/commands/cleanup")
    public String cleanUp();

    @PostMapping("/books/commands/borrow")
    public List<Book> borrowBooks(@RequestBody BooksRequest booksRequest);

    @PostMapping("/books/commands/return")
    public List<Book> returnBooks(@RequestBody BooksRequest booksRequest);

    /*@Component
    public static class BookClientFallback implements BookClient {

        @Override
        public List<Book> getBooks(Optional<Integer> pageNum, Optional<Integer> pageSize, Optional<Long> readerId) {
            return List.of();
        }

        @Override
        public List<Book> loadDatabase(Optional<Integer> count) {
            // No fallback
            throw new RuntimeException();
        }

        @Override
        public String cleanUp() {
            // No fallback
            throw new RuntimeException();
        }

        @Override
        public List<Book> borrowBooks(BooksRequest booksRequest) {
            return List.of();
        }

        @Override
        public List<Book> returnBooks(BooksRequest booksRequest) {
            return List.of();
        }
    }*/
}

