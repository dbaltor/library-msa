package reader.adapter.gateway;

import reader.dto.Book;
import reader.usecase.port.BookService;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;

import lombok.val;

@Service
public class BookServiceGateway implements BookService {
    @Value("${book.url}")
    private String BOOK_URL;

    public List<Book> findBooksByReaderId(long id) {
        RestTemplate restTemplate = new RestTemplate();
        try {
            val response = restTemplate.exchange(
                BOOK_URL + "/books?reader={id}", 
                HttpMethod.GET, 
                null,
                new ParameterizedTypeReference<List<Book>>() {},
                id);
            return response.getBody();
        } catch(RestClientException rce) {
            rce.printStackTrace();
            return List.of();
        }        
    }
}

