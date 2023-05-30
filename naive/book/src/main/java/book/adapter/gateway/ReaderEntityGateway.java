package book.adapter.gateway;

import book.domain.port.ReaderEntity;
import book.adapter.gateway.port.ReaderClient.ValidationRequest;
import book.dto.Book;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import lombok.val;

@Service
public class ReaderEntityGateway implements ReaderEntity {
    @Value("${reader.url}")
    private String READER_URL;
    
    public Set<BorrowingErrors> bookBorrowingValidator(Long readerId, List<Book> booksToBorrow, List<Book> borrowedBooks) {
        RestTemplate restTemplate = new RestTemplate();
        try {
            val response = restTemplate.postForObject(
                READER_URL + "/readers/{id}/commands/validatebookborrowing",
                new ValidationRequest(booksToBorrow, borrowedBooks),
                BorrowingErrors[].class,
                readerId);
            return Set.of(response);
            
            /*HttpHeaders requestHeaders = new HttpHeaders();
            requestHeaders.setContentType(MediaType.APPLICATION_JSON);
            requestHeaders.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
            val response = restTemplate.exchange("http://localhost:8082/readers/{id}/commands/validatebookborrowing",
                    HttpMethod.POST, new HttpEntity<ValidationRequest>(books, requestHeaders),
                    new ParameterizedTypeReference<Set<BorrowingErrors>>() {
                    }, readerId);
            return response.getBody(); */
        } catch (RestClientException e) {
            e.printStackTrace();
            return Set.of(BorrowingErrors.UNEXPECTED_ERROR);
        }
    }

    public Set<ReturningErrors> bookReturningValidator(Long readerId, List<Book> booksToReturn, List<Book> borrowedBooks) {
        RestTemplate restTemplate = new RestTemplate();
        try {
            val response = restTemplate.postForObject(
                READER_URL + "/readers/{id}/commands/validatebookreturning", 
                new ValidationRequest(booksToReturn, borrowedBooks),
                ReturningErrors[].class,
                readerId);
            return Set.of(response);
        } catch(RestClientException e) {
            e.printStackTrace();
            return Set.of(ReturningErrors.UNEXPECTED_ERROR);
        }
    }
}