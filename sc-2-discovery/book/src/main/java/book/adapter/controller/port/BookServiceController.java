package book.adapter.controller.port;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import book.adapter.controller.dto.BooksRequest;
import book.dto.Book;

import java.util.List;
import java.util.Optional;

@RequestMapping(value = "books",  produces = MediaType.APPLICATION_JSON_VALUE)
public interface BookServiceController {

    @GetMapping
    public List<Book> getBooks(
        @RequestParam("page") Optional<Integer> pageNum, 
        @RequestParam("size") Optional<Integer> pageSize,
        @RequestParam("reader") Optional<Long> readerId);

    @PostMapping("commands/load")
    public List<Book> loadDatabase(@RequestParam Optional<Integer> count);

    @PostMapping(value = "commands/cleanup", produces = MediaType.TEXT_PLAIN_VALUE)
    public String cleanUp(); 

    @PostMapping(value = "commands/borrow", consumes = MediaType.APPLICATION_JSON_VALUE)    
    public ResponseEntity<Object> borrowBooks(@RequestBody BooksRequest booksRequest);

    @PostMapping(value = "commands/return", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> returnBooks(@RequestBody BooksRequest booksRequest); 
}