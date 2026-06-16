package com.filmnema.filmnema_api.controller;

import com.filmnema.filmnema_api.dto.TestRequest;
import com.filmnema.filmnema_api.dto.TokenRequest;
import com.filmnema.filmnema_api.dto.TokenResponse;
import com.filmnema.filmnema_api.model.Movie;
import com.filmnema.filmnema_api.model.TestModel;
import com.filmnema.filmnema_api.service.TestService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/test")
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

    @PostMapping("/create")
    public ResponseEntity<TestModel> createTest(@Valid @RequestBody TestRequest request) {
        return ResponseEntity.ok(testService.createTest(request));
    }

    @PostMapping("/token")
    public ResponseEntity<TokenResponse> generateToken(@Valid @RequestBody TokenRequest request) {
        return ResponseEntity.ok(testService.generateToken(request));
    }
}
