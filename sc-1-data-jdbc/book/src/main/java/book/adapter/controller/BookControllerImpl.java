package book.adapter.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;

import book.dto.Book;
import book.adapter.controller.dto.BooksRequest;
import book.adapter.controller.port.BookController;
import book.usecase.port.BookService;
import book.usecase.port.ReaderService;
import book.usecase.exception.BorrowingException;
import book.usecase.exception.ReturningException;
import book.domain.port.ReaderEntity;

import java.util.List;
import java.util.Optional;
import java.util.Arrays;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.val;

@RestController
@RequiredArgsConstructor
public class BookControllerImpl implements BookController {

    private final @NonNull BookService bookService;
    private final @NonNull ReaderService readerService;

    @Override
    public List<Book> retrieveBooks(
        Optional<Integer> pageNum, 
        Optional<Integer> pageSize,
        Optional<Long> readerId) {
            if (readerId.isPresent()) {
                //retrieve books borrowed by reader
                return bookService.findBooksByReaderId(readerId.get());
            }
            else {
                // Retrieve books
                return bookService.retrieveBooks(pageNum, pageSize);
            }
    }

    @Override
    public List<Book> loadDatabase(Optional<Integer> count) {
        return  bookService.loadDatabase(
            count,
            readerService.retrieveReaders(
                Optional.empty(),
                Optional.empty(),
                Optional.empty())
        );
    }

    @Override
    public String cleanUp() {
        bookService.cleanUpDatabase();
        return "The data have been removed";
    }

    @Override
    public ResponseEntity<Object> borrowBooks(BooksRequest booksRequest) {
            val readerId = booksRequest.readerId;
            val bookIds = booksRequest.bookIds;
            if (bookIds == null || bookIds.length == 0) {
                return ResponseEntity
                        .badRequest()
                        .body("No books provided. Nothing to do.");
            }
            val booksToBorrow = bookService.findBooksByIds(Arrays.asList(bookIds));
            try{ 
                val borrowedBooks = bookService.borrowBooks(booksToBorrow, readerId);
                return ResponseEntity.ok(borrowedBooks);
            } catch(BorrowingException e) {
                val errorMsg = new StringBuilder("Errors found:");
                for(ReaderEntity.BorrowingErrors error : e.errors) {
                    switch (error) {
                        case READER_NOT_FOUND:
                            errorMsg.append(String.format(" *No reader with ID %d has been found.", readerId));
                            break;
                        case MAX_BORROWED_BOOKS_EXCEEDED:
                            errorMsg.append(" *Maximum allowed borrowed books exceeded.");
                            break;
                        default:
                            errorMsg.append(" *Unexpected error.");
                    }
                }
                return ResponseEntity
                        .badRequest()
                        .body(errorMsg.toString());
            }
    }

    @Override
    public ResponseEntity<Object> returnBooks(
        BooksRequest booksRequest) {
            val readerId = booksRequest.readerId;
            val bookIds = booksRequest.bookIds;
            if (bookIds == null || bookIds.length == 0) {
                return ResponseEntity
                    .badRequest()
                    .body("No books provided. Nothing to do.");
            }    
            val booksToReturn = bookService.findBooksByIds(Arrays.asList(bookIds));
            try{ 
                val returnedBooks = bookService.returnBooks(booksToReturn, readerId);
                return ResponseEntity.ok(returnedBooks);
            } catch(ReturningException e) {
                val errorMsg = new StringBuilder("Errors found:");
                for(ReaderEntity.ReturningErrors error : e.errors) {
                    switch (error) {
                        case READER_NOT_FOUND:
                            errorMsg.append(String.format(" *No reader with ID %d has been found.", readerId));
                            break;
                        default:
                            errorMsg.append(" *Unexpected error.");
                    }
                }
                return ResponseEntity
                    .badRequest()
                    .body(errorMsg.toString());
            }
    }
}
