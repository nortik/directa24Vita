package com.directa24.main.challenge.service.impl;

import java.util.*;

import com.directa24.main.challenge.exception.JsonRequestException;
import com.directa24.main.challenge.model.Movie;
import com.directa24.main.challenge.model.RequestDto;
import com.directa24.main.challenge.service.DirectorService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class DirectorServiceImpl implements DirectorService {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${api.movies.url}")
    private String apiUrl;  // we have that URL in application.yml

    public List<String> getDirectors(int threshold) {
        Map<String, Integer> directorMap = new HashMap<>();
        ObjectMapper objectMapper = new ObjectMapper();
        int currentPage = 1;
        int totalPage = 1;
        try {
            while (currentPage <= totalPage)
            {
                ResponseEntity<byte[]> responseEntity =
                        restTemplate.getForEntity(apiUrl + currentPage, byte[].class);
                if (responseEntity.getBody() != null) {
                    RequestDto response = objectMapper.readValue(responseEntity.getBody(), RequestDto.class);
                    totalPage = response.getTotalPages();
                    List<Movie> movies = response.getData();
                    for (Movie movie : movies) {
                        String director = movie.getDirector();
                        directorMap.merge(director, 1, Integer::sum);  // Increase the counter.
                    }
                }
                currentPage++;
            }
        } catch (Exception e) {
            throw new JsonRequestException("Error reading JSON file from API: "
                    +apiUrl+threshold);
        }

        //Sorted List of Directors
        List<String> result = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : directorMap.entrySet()) {
            if (entry.getValue() > threshold) {
                result.add(entry.getKey());
            }
        }
        Collections.sort(result);
        return result;
    }
}


