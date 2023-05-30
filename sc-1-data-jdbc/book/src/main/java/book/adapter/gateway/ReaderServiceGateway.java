package book.adapter.gateway;

import book.dto.Reader;
import book.usecase.port.ReaderService;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;

import lombok.val;

@Service
public class ReaderServiceGateway implements ReaderService {
    @Value("${reader.url}")
    private String READER_URL;
    
    public List<Reader> retrieveReaders(
        Optional<Integer> pageNum,
        Optional<Integer> pageSize,
        Optional<Long> readerId) {
            RestTemplate restTemplate = new RestTemplate();
            try {

                UriComponentsBuilder uriBuilder = UriComponentsBuilder
                    .fromHttpUrl(READER_URL + "/readers");
                if (pageNum.isPresent())
                    uriBuilder.queryParam("page", pageNum.get());
                if (pageSize.isPresent())
                    uriBuilder.queryParam("size", pageSize.get());
                if (readerId.isPresent())
                    uriBuilder.queryParam("readerId", readerId.get());
                val response = restTemplate.exchange(
                    uriBuilder.toUriString(), 
                    HttpMethod.GET, 
                    null,
                    new ParameterizedTypeReference<List<Reader>>() {});
                return response.getBody();
            } catch(RestClientException rce) {
                rce.printStackTrace();
                return List.of();
            }     
    }
}
