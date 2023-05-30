package book.adapter.controller.dto;

import lombok.Data;
import lombok.NonNull;

@Data
public class BooksRequest {
    public @NonNull Long readerId;
    public @ NonNull 
    Long bookIds[];
}
