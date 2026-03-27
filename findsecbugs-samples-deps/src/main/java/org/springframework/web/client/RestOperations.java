package org.springframework.web.client;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;

import java.net.URI;
import java.util.Map;
import java.util.Set;

public interface RestOperations {
    // GET

    <T> T getForObject(String url, Class<T> responseType, Object... uriVariables) throws RestClientException;

    <T> T getForObject(String url, Class<T> responseType, Map<String, ?> uriVariables) throws RestClientException;

    <T> T getForObject(URI url, Class<T> responseType) throws RestClientException;

    <T> ResponseEntity<T> getForEntity(String url, Class<T> responseType, Object... uriVariables)
            throws RestClientException;

    <T> ResponseEntity<T> getForEntity(String url, Class<T> responseType, Map<String, ?> uriVariables)
            throws RestClientException;

    <T> ResponseEntity<T> getForEntity(URI url, Class<T> responseType) throws RestClientException;


    // HEAD

    HttpHeaders headForHeaders(String url, Object... uriVariables) throws RestClientException;

    HttpHeaders headForHeaders(String url, Map<String, ?> uriVariables) throws RestClientException;

    HttpHeaders headForHeaders(URI url) throws RestClientException;


    // POST

    URI postForLocation(String url,  Object request, Object... uriVariables) throws RestClientException;

    URI postForLocation(String url,  Object request, Map<String, ?> uriVariables)
            throws RestClientException;

    URI postForLocation(URI url,  Object request) throws RestClientException;

    <T> T postForObject(String url,  Object request, Class<T> responseType,
                        Object... uriVariables) throws RestClientException;

    <T> T postForObject(String url,  Object request, Class<T> responseType,
                        Map<String, ?> uriVariables) throws RestClientException;

    <T> T postForObject(URI url,  Object request, Class<T> responseType) throws RestClientException;

    <T> ResponseEntity<T> postForEntity(String url,  Object request, Class<T> responseType,
                                        Object... uriVariables) throws RestClientException;

    <T> ResponseEntity<T> postForEntity(String url,  Object request, Class<T> responseType,
                                        Map<String, ?> uriVariables) throws RestClientException;

    <T> ResponseEntity<T> postForEntity(URI url,  Object request, Class<T> responseType)
            throws RestClientException;


    // PUT

    void put(String url,  Object request, Object... uriVariables) throws RestClientException;

    void put(String url,  Object request, Map<String, ?> uriVariables) throws RestClientException;

    void put(URI url,  Object request) throws RestClientException;


    // PATCH

    <T> T patchForObject(String url,  Object request, Class<T> responseType, Object... uriVariables)
            throws RestClientException;

    <T> T patchForObject(String url,  Object request, Class<T> responseType,
                         Map<String, ?> uriVariables) throws RestClientException;

    <T> T patchForObject(URI url,  Object request, Class<T> responseType)
            throws RestClientException;



    // DELETE

    void delete(String url, Object... uriVariables) throws RestClientException;

    void delete(String url, Map<String, ?> uriVariables) throws RestClientException;

    void delete(URI url) throws RestClientException;


    // OPTIONS

    Set<HttpMethod> optionsForAllow(String url, Object... uriVariables) throws RestClientException;

    Set<HttpMethod> optionsForAllow(String url, Map<String, ?> uriVariables) throws RestClientException;

    Set<HttpMethod> optionsForAllow(URI url) throws RestClientException;


    // exchange

    <T> ResponseEntity<T> exchange(String url, HttpMethod method,  HttpEntity<?> requestEntity,
                                   Class<T> responseType, Object... uriVariables) throws RestClientException;

    <T> ResponseEntity<T> exchange(String url, HttpMethod method,  HttpEntity<?> requestEntity,
                                   Class<T> responseType, Map<String, ?> uriVariables) throws RestClientException;

    <T> ResponseEntity<T> exchange(URI url, HttpMethod method,  HttpEntity<?> requestEntity,
                                   Class<T> responseType) throws RestClientException;

    <T> ResponseEntity<T> exchange(String url, HttpMethod method, HttpEntity<?> requestEntity,
                                   ParameterizedTypeReference<T> responseType, Object... uriVariables) throws RestClientException;

    <T> ResponseEntity<T> exchange(String url, HttpMethod method,  HttpEntity<?> requestEntity,
                                   ParameterizedTypeReference<T> responseType, Map<String, ?> uriVariables) throws RestClientException;

    <T> ResponseEntity<T> exchange(URI url, HttpMethod method,  HttpEntity<?> requestEntity,
                                   ParameterizedTypeReference<T> responseType) throws RestClientException;

    <T> ResponseEntity<T> exchange(RequestEntity<?> requestEntity, Class<T> responseType)
            throws RestClientException;

    <T> ResponseEntity<T> exchange(RequestEntity<?> requestEntity, ParameterizedTypeReference<T> responseType)
            throws RestClientException;


    // General execution


    <T> T execute(String url, HttpMethod method,  RequestCallback requestCallback,
                  ResponseExtractor<T> responseExtractor, Object... uriVariables)
            throws RestClientException;


    <T> T execute(String url, HttpMethod method,  RequestCallback requestCallback,
                  ResponseExtractor<T> responseExtractor, Map<String, ?> uriVariables)
            throws RestClientException;


    <T> T execute(URI url, HttpMethod method,  RequestCallback requestCallback,
                  ResponseExtractor<T> responseExtractor) throws RestClientException;

}
