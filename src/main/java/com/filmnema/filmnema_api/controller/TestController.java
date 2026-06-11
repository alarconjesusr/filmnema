package com.filmnema.filmnema_api.controller;

import com.filmnema.filmnema_api.model.Movie;
import com.filmnema.filmnema_api.service.TestService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    private final TestService testService;

    public TestController(TestService testService) {
        this.testService = testService;
    }

    @GetMapping("/test")
    public ResponseEntity<Movie> testEndpoint() {
        return testService.getFirstMovie()
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
