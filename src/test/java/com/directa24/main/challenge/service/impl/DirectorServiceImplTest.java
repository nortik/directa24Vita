package com.directa24.main.challenge.service.impl;

import com.directa24.main.challenge.exception.JsonRequestException;
import com.directa24.main.challenge.model.Movie;
import com.directa24.main.challenge.model.RequestDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@SpringBootTest
//@TestPropertySource(properties =
//        "api.movies.url=https://eron-movies.wiremockapi.cloud/api/movies/search?page=")
class DirectorServiceImplTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private DirectorServiceImpl directorService;

    @Value("${api.movies.url}")
    private String apiUrl;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        objectMapper = new ObjectMapper();
    }

    @Test
    void testGetDirectors_NotFound() throws Exception {
        // Mock -> empty data
        RequestDto mockRequest = new RequestDto();
        mockRequest.setTotalPages(1);
        mockRequest.setData(Collections.emptyList());
        ResponseEntity<byte[]> responseEntity = ResponseEntity.ok(objectMapper.writeValueAsBytes(mockRequest));
        when(restTemplate.getForEntity(anyString(), eq(byte[].class))).thenReturn(responseEntity);
        List<String> directors = directorService.getDirectors(1);

        assertNotNull(directors);
        assertTrue(directors.isEmpty());
    }

    @Test
    void testGetDirectors_ExceptionHandling() {
        // Failure call in the RestTemplate
        when(restTemplate.getForEntity(anyString(), eq(byte[].class))).
                thenThrow(new RuntimeException("API error"));
        JsonRequestException thrown = assertThrows(JsonRequestException.class, () -> {
            directorService.getDirectors(1);
        });

        assertTrue(thrown.getMessage().contains("Error reading JSON file from API"));
    }

    @Test
    void testGetDirectors_Success() throws Exception {
        // Mock
        RequestDto mockRequest = new RequestDto();
        mockRequest.setTotalPages(1);
        mockRequest.setData(Arrays.asList(
                new Movie("Movie1", "2020", "PG",
                        "2020-05-01", "60 min", "Action",
                        "Director1", "Writer1", "Actor1, Actor2"),
                new Movie("Movie2", "2021", "PG",
                        "2021-06-01", "99 min", "Drama",
                        "Director2", "Writer2", "Actor1, Actor3")
        ));

        ResponseEntity<byte[]> responseEntity = ResponseEntity.ok(objectMapper.writeValueAsBytes(mockRequest));
        when(restTemplate.getForEntity(anyString(), eq(byte[].class))).thenReturn(responseEntity);
        List<String> directors = directorService.getDirectors(1);

        assertNotNull(directors);
    }
}