package book.adapter.gateway.port;

import book.domain.port.ReaderEntity.BorrowingErrors;
import book.domain.port.ReaderEntity.ReturningErrors;
import book.dto.Book;
import book.dto.Reader;

import org.springframework.cloud.openfeign.FeignClient;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Data;
import lombok.NonNull;

//@FeignClient(name = "library-reader-service", fallback = ReaderClient.ReaderClientFallback.class)
@FeignClient(name = "library-reader-service")
public interface ReaderClient{
    @Data
    @NoArgsConstructor @AllArgsConstructor
    public class ValidationRequest {
        private @NonNull List<Book> booksToValidate;
        private @NonNull List<Book> borrowedBooksByReader;
    }

     /**
     * Retrieve the list of readers currently stored in the Reader datastore
     * @param pageNum   the number of the page to be retrieved.
     *                  No pagination is to apply when this is not present
     * @param pageSize  the number of items per page when pagination is being applied
     * @param readerId  the id of the reader currently borrowing the books, if any.
     * @return          the list of readers retrieved
     */
    @GetMapping("/readers")
    public List<Reader> getReaders(
        @RequestParam("page") Optional<Integer> pageNum,
        @RequestParam("size") Optional<Integer> pageSize,
        @RequestParam("reader") Optional<Long> readerId);

    @PostMapping("/readers/commands/load")
    public List<Reader> loadDatabase(@RequestParam Optional<Integer> count);

    @PostMapping("/readers/commands/cleanup")
    public String cleanUp();

    @PostMapping("/readers/{id}/commands/validatebookborrowing")
    public Set<BorrowingErrors> validateBookBorrowing(
        @PathVariable(name = "id") Long readerId, 
        @RequestBody ValidationRequest validationRequest);

    @PostMapping("readers/{id}/commands/validatebookreturning")
    public Set<ReturningErrors> validateBookReturning(
        @PathVariable(name = "id") Long readerId, 
        @RequestBody ValidationRequest validationRequest);

    /*@Component
    public static class ReaderClientFallback implements ReaderClient{
        @Value("${book.default-max-allowed-borrowed-books}")
        private int DEFAULT_MAX_ALLOWED_BORROWED_BOOKS;
        
        @Override
        public List<Reader> getReaders(Optional<Integer> pageNum, Optional<Integer> pageSize, Optional<Long> readerId) {
            return List.of();
        }

        @Override
        public List<Reader> loadDatabase(Optional<Integer> count) {
            // No fallback
            throw new RuntimeException();
        }

        @Override
        public String cleanUp() {
            // No fallback
            throw new RuntimeException();
        }

        @Override
        public Set<BorrowingErrors> validateBookBorrowing(Long readerId, List<Book> books) {
            if (books.size() <= DEFAULT_MAX_ALLOWED_BORROWED_BOOKS) {
                return Set.of();
            }
            // No fallback
            throw new RuntimeException();
        }

        @Override
        public Set<ReturningErrors> validateBookReturning(Long readerId, List<Book> books) {
            return Set.of();
        }
    }*/

}
