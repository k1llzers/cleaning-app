package com.ukma.cleaning;

import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.ukma.cleaning.utils.security.dto.AuthRequest;
import com.ukma.cleaning.utils.security.dto.JwtResponse;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TestTRT {
    private static String jwtCookie;

    public static void clear(){
        jwtCookie = "";
        SecurityContextHolder.clearContext();
    }

    public static void authAs(long id, int port)
    {
        SecurityContextHolder.clearContext();
        String username;
        String password;
        
        switch ((int)id) {
            case 1:
                username = "admin";
                password = "admin";
                break;
            case 2:
                username = "m.burnatt@gmail.com";
                password = "Qw3rty*";
                break;
            case 3:
                username = "c.burnett@outlook.com";
                password = "P4ssw()rd";
                break;
            case 4:
                username = "b.durman@gmail.com";
                password = "password";
                break;
            case 5:
                username = "m.jackobs@gmail.com";
                password = "us3r";
                break;
            case 6:
                username = "k.charles@i.ua";
                password = "qwerty";
                break;
        
            default:
            throw new IllegalArgumentException("Invalid id");
        }

        RestTemplate restTemplate = new RestTemplate();

        AuthRequest authRequest = new AuthRequest();
        authRequest.setUsername(username);
        authRequest.setPassword(password);

        try {
            ResponseEntity<JwtResponse> response = restTemplate.postForEntity("http://localhost:" + port + "/api/auth/login", authRequest, JwtResponse.class);
            if (response.getStatusCode() == HttpStatus.OK) {
                JwtResponse responseBody = response.getBody();
                jwtCookie = "accessToken=" + responseBody.getAccessToken() + "; refreshToken=" + responseBody.getRefreshToken();
            } else {
                log.warn("Received status code: " + response.getStatusCode().toString());
            }
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatus.UNAUTHORIZED) {
                log.warn("Unauthorized - Invalid credentials provided");
            } else {
                log.warn("HTTP error occurred: " + e.getStatusCode());
            }
        } catch (Exception e) {
            log.warn("An error occurred: " + e.getMessage());
        }
    }

    private static HttpHeaders headers(){
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cookie", jwtCookie);
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }

    public static <T> ResponseEntity<T> exchange(String url, HttpMethod method, Class<T> responseType, Object... uriVariables){
        HttpEntity<?> request = new HttpEntity<>(headers());
        TestRestTemplate restTemplate = new TestRestTemplate();
        return restTemplate.exchange(url, method, request, responseType, uriVariables);
    }

    public static <T> ResponseEntity<T> get(String url, Class<T> responseType, Object... uriVariables){
        return exchange(url, HttpMethod.GET, responseType, uriVariables);
    }

    public static <T> ResponseEntity<T> post(String url, Object request, Class<T> responseType, Object... urlVariables){
        HttpEntity<?> _request = new HttpEntity<>(request, headers());
        TestRestTemplate restTemplate = new TestRestTemplate();
        return restTemplate.postForEntity(url, _request, responseType, urlVariables);
    }

    public static <T> ResponseEntity<T> put(String url, Object request, Class<T> T, Object... urlVariables){
        HttpEntity<?> _request = new HttpEntity<>(request, headers());
        TestRestTemplate restTemplate = new TestRestTemplate();
        return restTemplate.exchange(url, HttpMethod.PUT, _request, T, (Object)urlVariables);
    }

    public static <T> ResponseEntity<T> putNothing(String url, Class<T> T, Object... urlVariables){
        HttpEntity<?> _request = new HttpEntity<>(headers());
        TestRestTemplate restTemplate = new TestRestTemplate();
        return restTemplate.exchange(url, HttpMethod.PUT, _request, T, (Object)urlVariables);
    }

    public static boolean delete(String url, Object... urlVariables){
        HttpEntity<?> request = new HttpEntity<>(headers());
        TestRestTemplate restTemplate = new TestRestTemplate();
        try {
            var response = restTemplate.exchange(url, HttpMethod.DELETE, request, String.class, urlVariables);
            log.warn(response.getBody());
            return !response.getBody().startsWith("<!DOCTYPE html>");
        } catch (Exception e) {
            return false;
        }
    }

}
