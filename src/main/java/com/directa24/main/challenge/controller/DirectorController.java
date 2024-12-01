package com.directa24.main.challenge.controller;

import com.directa24.main.challenge.exception.BadRequestException;
import com.directa24.main.challenge.service.DirectorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public class DirectorController {

    @Autowired
    private DirectorService directorService;

    @GetMapping("/api/directors")
    public ResponseEntity<Map<String, List<String>>> getDirectors(
            @RequestParam int threshold) {
        if (threshold <= 0) {
            throw new BadRequestException("Threshold params MUST be greater than 0");
        }
        List<String> directors = directorService.getDirectors(threshold);
        return new ResponseEntity(Map.of("directors", directors), HttpStatus.OK);
    }
}

