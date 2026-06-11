package com.filmnema.filmnema_api.service;

import com.filmnema.filmnema_api.model.Movie;
import com.filmnema.filmnema_api.repository.TestRepository;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class TestService {

    private final TestRepository testRepository;

    public TestService(TestRepository testRepository) {
        this.testRepository = testRepository;
    }

    public Optional<Movie> getFirstMovie() {
        return testRepository.findFirstMovie();
    }
}