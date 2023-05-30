package library.adapter.gateway;

import library.dto.Reader;
import library.usecase.port.ReaderService;
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
                    uriBuilder.queryParam("reader", readerId.get());
                val response = restTemplate.exchange(
                    uriBuilder.toUriString(), 
                    HttpMethod.GET, 
                    null,
                    new ParameterizedTypeReference<List<Reader>>() {});
                return response.getBody();
            } catch (RestClientException e) {
                e.printStackTrace();
                return List.of();
            }   
    }

    public List<Reader> loadDatabase(Optional<Integer> nReaders) throws RequestException {
        RestTemplate restTemplate = new RestTemplate();
        try {
            UriComponentsBuilder uriBuilder = UriComponentsBuilder
                .fromHttpUrl(READER_URL + "/readers/commands/load");
            if (nReaders.isPresent())
                uriBuilder.queryParam("count", nReaders.get());
            val response = restTemplate.exchange(
                uriBuilder.toUriString(), 
                HttpMethod.POST, 
                null,
                new ParameterizedTypeReference<List<Reader>>() {});
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
                READER_URL + "/readers/commands/cleanup", 
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
}
