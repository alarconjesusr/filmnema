package com.filmnema.filmnema_api.service;

import com.filmnema.filmnema_api.dto.TestRequest;
import com.filmnema.filmnema_api.dto.TokenRequest;
import com.filmnema.filmnema_api.dto.TokenResponse;
import com.filmnema.filmnema_api.mapper.TestMapper;
import com.filmnema.filmnema_api.model.Movie;
import com.filmnema.filmnema_api.model.TestModel;
import com.filmnema.filmnema_api.repository.TestRepository;
import java.util.Optional;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;

@Service
public class TestService {

	private static final String HARD_CODED_USERNAME = "admin";
	private static final String HARD_CODED_PASSWORD = "admin123";

    private final TestRepository testRepository;
    private final TestMapper testMapper;
    private final JwtService jwtService;

    public TestService(TestRepository testRepository, TestMapper testMapper, JwtService jwtService) {
        this.testRepository = testRepository;
        this.testMapper = testMapper;
        this.jwtService = jwtService;
    }

    public Optional<Movie> getFirstMovie() {
        return testRepository.findFirstMovie();
    }

    public TestModel createTest(TestRequest request) {
        return testMapper.toModel(request);
    }

    public TokenResponse generateToken(TokenRequest request) {
        if (!HARD_CODED_USERNAME.equals(request.username()) || !HARD_CODED_PASSWORD.equals(request.password())) {
            throw new BadCredentialsException("Invalid credentials");
        }

        return new TokenResponse(jwtService.generateToken(HARD_CODED_USERNAME), "Bearer");
    }
}