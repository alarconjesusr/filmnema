package com.filmnema.filmnema_api.config;

import com.filmnema.filmnema_api.domain.Event;
import com.filmnema.filmnema_api.repository.EventRepository;
import java.time.LocalDateTime;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("h2")
public class H2DataInitializer {

	@Bean
	CommandLineRunner loadSampleEvents(EventRepository eventRepository) {
		return args -> {
			if (eventRepository.count() > 0) {
				return;
			}

			eventRepository.save(new Event(
					"H2 kickoff",
					"Sample event loaded into the in-memory database.",
					LocalDateTime.now().plusDays(1)
			));

			eventRepository.save(new Event(
					"Design review",
					"Second sample event for query testing.",
					LocalDateTime.now().plusDays(2)
			));
		};
	}
}