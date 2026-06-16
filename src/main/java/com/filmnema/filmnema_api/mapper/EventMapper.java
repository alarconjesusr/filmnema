package com.filmnema.filmnema_api.mapper;

import com.filmnema.filmnema_api.domain.Event;
import com.filmnema.filmnema_api.dto.EventRequest;
import com.filmnema.filmnema_api.dto.EventResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface EventMapper {

	@Mapping(target = "id", ignore = true)
	@Mapping(target = "createdAt", ignore = true)
	Event toEntity(EventRequest request);

	EventResponse toResponse(Event event);
}