package library.adapter.gateway;

import library.dto.Book;
import library.usecase.port.BookService;
import library.usecase.exception.RequestException;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpEntity;

import lombok.val;

@Service
public class BookServiceGateway implements BookService {
    @Value("${book.url}")
    private String BOOK_URL;

    public List<Book> retrieveBooks(
        Optional<Integer> pageNum, 
        Optional<Integer> pageSize, 
        Optional<Long> readerId) {
            RestTemplate restTemplate = new RestTemplate();
            try {

                UriComponentsBuilder uriBuilder = UriComponentsBuilder
                    .fromHttpUrl(BOOK_URL + "/books");
                if (pageNum.isPresent())
                    uriBuilder.queryParam("page", pageNum.get());
                if (pageSize.isPresent())
                    uriBuilder.queryParam("size", pageSize.get());
                if (readerId.isPresent())
                    uriBuilder.queryParam("reader", readerId.get());
                val response = restTemplate.exchange(
                    uriBuilder.toUriString(), 
                    HttpMethod.GET, 
                    null,
                    new ParameterizedTypeReference<List<Book>>() {});
                return response.getBody();
            } catch (RestClientException e) {
                e.printStackTrace();
                return List.of();
            }
    }

    public List<Book> loadDatabase(Optional<Integer> nBooks) throws RequestException {
        RestTemplate restTemplate = new RestTemplate();
        try {
            UriComponentsBuilder uriBuilder = UriComponentsBuilder
                .fromHttpUrl(BOOK_URL + "/books/commands/load");
            if (nBooks.isPresent())
                uriBuilder.queryParam("count", nBooks.get());
            val response = restTemplate.exchange(
                uriBuilder.toUriString(), 
                HttpMethod.POST, 
                null,
                new ParameterizedTypeReference<List<Book>>() {});
            return response.getBody();
        } catch (RestClientResponseException e) {
            //e.printStackTrace();
            throw new RequestException(
                e.getRawStatusCode(),
                e.getResponseBodyAsString());
        } catch (RestClientException e) {
            e.printStackTrace();
            throw new RequestException(500, "Unexpected Error.");            
        }
    }

    public void cleanUpDatabase() throws RequestException {
        RestTemplate restTemplate = new RestTemplate();
        try {
            restTemplate.postForLocation(
                BOOK_URL + "/books/commands/cleanup", 
                null); 
        } catch (RestClientResponseException e) {
            //e.printStackTrace();
            throw new RequestException(
                e.getRawStatusCode(),
                e.getResponseBodyAsString());
        } catch (RestClientException e) {
            e.printStackTrace();
            throw new RequestException(500, "Unexpected Error.");            
        }
    }
    
    public List<Book> borrowBooks(BookServiceRequest bookServiceRequest) throws RequestException {    
        RestTemplate restTemplate = new RestTemplate();
        try {
            val response = restTemplate.exchange(
                BOOK_URL + "/books/commands/borrow",
                HttpMethod.POST,
                new HttpEntity<BookServiceRequest>(bookServiceRequest, null),
                new ParameterizedTypeReference<List<Book>>() {});
            return response.getBody();
        } catch (RestClientResponseException e) {
            //e.printStackTrace();
            throw new RequestException(
                e.getRawStatusCode(),
                e.getResponseBodyAsString());
        } catch (RestClientException e) {
            e.printStackTrace();
            throw new RequestException(500, "Unexpected Error.");            
        }
    }

    public List<Book> returnBooks(BookServiceRequest bookServiceRequest) throws RequestException {    
        RestTemplate restTemplate = new RestTemplate();
        try {
            val response = restTemplate.exchange(
                BOOK_URL + "/books/commands/return",
                HttpMethod.POST,
                new HttpEntity<BookServiceRequest>(bookServiceRequest, null),
                new ParameterizedTypeReference<List<Book>>() {});
            return response.getBody();
        } catch (RestClientResponseException e) {
            //e.printStackTrace();
            throw new RequestException(
                e.getRawStatusCode(),
                e.getResponseBodyAsString());
        } catch (RestClientException e) {
            e.printStackTrace();
            throw new RequestException(500, "Unexpected Error.");            
        }
    }
}
