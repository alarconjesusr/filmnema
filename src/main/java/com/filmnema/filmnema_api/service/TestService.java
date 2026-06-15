package com.filmnema.filmnema_api.service;

import com.filmnema.filmnema_api.model.Movie;
import com.filmnema.filmnema_api.dto.TestRequest;
import com.filmnema.filmnema_api.mapper.TestMapper;
import com.filmnema.filmnema_api.model.TestModel;
import com.filmnema.filmnema_api.repository.TestRepository;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class TestService {

    private final TestRepository testRepository;
    private final TestMapper testMapper;

    public TestService(TestRepository testRepository, TestMapper testMapper) {
        this.testRepository = testRepository;
        this.testMapper = testMapper;
    }

    public Optional<Movie> getFirstMovie() {
        return testRepository.findFirstMovie();
    }

    public TestModel createTest(TestRequest request) {
        return testMapper.toModel(request);
    }
}