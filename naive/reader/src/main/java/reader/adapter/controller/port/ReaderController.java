package reader.adapter.controller.port;

import reader.domain.port.ReaderEntity.BorrowingErrors;
import reader.domain.port.ReaderEntity.ReturningErrors;
import reader.dto.Book;
import reader.dto.Reader;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;

@RequestMapping(value = "readers",  produces = MediaType.APPLICATION_JSON_VALUE)
public interface ReaderController {
    @Data
    @AllArgsConstructor
    public class ValidationRequest {
        private @NonNull List<Book> booksToValidate;
        private @NonNull List<Book> borrowedBooksByReader;
    }

    @GetMapping
    public List<Reader> getReaders(
        @RequestParam(name = "page") Optional<Integer> pageNum,
        @RequestParam(name = "size") Optional<Integer> pageSize,
        @RequestParam(name = "reader") Optional<Long> readerId);

    @PostMapping("commands/load")
    public List<Reader> loadDatabase(@RequestParam Optional<Integer> count);

    @PostMapping(value = "commands/cleanup", produces = MediaType.TEXT_PLAIN_VALUE)
    public String cleanUp();

    @PostMapping("{id}/commands/validatebookborrowing")
    public Set<BorrowingErrors> validateBookBorrowing(
        @PathVariable(name = "id") Long readerId, 
        @RequestBody ValidationRequest validationRequest);

    @PostMapping("{id}/commands/validatebookreturning")
    public Set<ReturningErrors> validateBookReturning(
        @PathVariable(name = "id") Long readerId, 
        @RequestBody ValidationRequest validationRequest);
}