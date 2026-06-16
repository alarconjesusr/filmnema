package com.filmnema.filmnema_api.controller;

import com.filmnema.filmnema_api.dto.EventRequest;
import com.filmnema.filmnema_api.dto.EventResponse;
import com.filmnema.filmnema_api.service.EventService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/events")
public class EventController {

	private final EventService eventService;

	public EventController(EventService eventService) {
		this.eventService = eventService;
	}

	@PostMapping
	public ResponseEntity<EventResponse> createEvent(@Valid @RequestBody EventRequest request) {
		return ResponseEntity.status(HttpStatus.CREATED).body(eventService.createEvent(request));
	}

	@GetMapping
	public ResponseEntity<List<EventResponse>> getEvents() {
		return ResponseEntity.ok(eventService.findAllEvents());
	}

	@GetMapping("/{id}")
	public ResponseEntity<EventResponse> getEventById(@PathVariable Long id) {
		return eventService.findEventById(id)
				.map(ResponseEntity::ok)
				.orElseGet(() -> ResponseEntity.notFound().build());
	}
}