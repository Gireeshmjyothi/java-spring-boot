package com.example.practice.service.external;

import com.example.practice.exception.CustomException;
import com.example.practice.model.request.ExternalUserRequest;
import com.example.practice.util.ErrorConstants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import org.springframework.http.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ClientService {

    private final RestTemplate restTemplate;

    public Object getDataFromClient (Optional<Integer> id){
        log.info("Fetching data from client API.");
        try{
            HttpHeaders httpHeaders = prepareHttpHeaders();
            HttpEntity<Object> httpEntity = new HttpEntity<>(null, httpHeaders);
            UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromUriString("https://reqres.in/api");
            if (id.isPresent()) {
                uriBuilder.path("/users/" + id.get());
            } else {
                uriBuilder.path("/users");
            }

            URI uri = uriBuilder.build().toUri();
            log.info("Prepared request for client API.");
            return restTemplate.exchange(uri, HttpMethod.GET, httpEntity, Object.class).getBody();
        }catch (Exception exp){
            log.error("error while calling client get API {}", exp.getMessage());
            throw new CustomException(ErrorConstants.CLIENT_ERROR_CODE, ErrorConstants.CLIENT_ERROR_MESSAGE);
        }
    }

    public Object postDataToClient(ExternalUserRequest user){
        log.info("Posting data from client API.");
        try{
            HttpHeaders httpHeaders = prepareHttpHeaders();
            HttpEntity<Object> httpEntity = new HttpEntity<>(user, httpHeaders);
            URI uri = UriComponentsBuilder.fromUriString("https://reqres.in/api")
                    .path("/users")
                    .build()
                    .toUri();
            log.info("Prepared user request for client API.");
            return restTemplate.exchange(uri, HttpMethod.POST, httpEntity, Object.class).getBody();
        }catch (Exception exp){
            log.error("error while calling client post API {}", exp.getMessage());
            throw new CustomException(ErrorConstants.CLIENT_ERROR_CODE, ErrorConstants.CLIENT_ERROR_MESSAGE);
        }
    }

    private HttpHeaders prepareHttpHeaders(){
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.add(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
        return httpHeaders;
    }
}
