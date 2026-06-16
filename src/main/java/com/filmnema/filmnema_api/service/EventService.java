package com.filmnema.filmnema_api.service;

import com.filmnema.filmnema_api.domain.Event;
import com.filmnema.filmnema_api.dto.EventRequest;
import com.filmnema.filmnema_api.dto.EventResponse;
import com.filmnema.filmnema_api.mapper.EventMapper;
import com.filmnema.filmnema_api.repository.EventRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class EventService {

	private final EventRepository eventRepository;
	private final EventMapper eventMapper;

	public EventService(EventRepository eventRepository, EventMapper eventMapper) {
		this.eventRepository = eventRepository;
		this.eventMapper = eventMapper;
	}

	public EventResponse createEvent(EventRequest request) {
		Event event = eventMapper.toEntity(request);
		Event savedEvent = eventRepository.save(event);
		return eventMapper.toResponse(savedEvent);
	}

	public List<EventResponse> findAllEvents() {
		return eventRepository.findAllByOrderByStartsAtAsc().stream()
				.map(eventMapper::toResponse)
				.toList();
	}

	public Optional<EventResponse> findEventById(Long id) {
		return eventRepository.findById(id).map(eventMapper::toResponse);
	}
}