package book.adapter.gateway.port;

import book.dto.Book;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Data;
import lombok.NonNull;

public interface ReaderClient{
    @Data
    @NoArgsConstructor @AllArgsConstructor
    public class ValidationRequest {
        private @NonNull List<Book> booksToValidate;
        private @NonNull List<Book> borrowedBooksByReader;
    }

}