package com.filmnema.filmnema_api.mapper;

import com.filmnema.filmnema_api.dto.TestRequest;
import com.filmnema.filmnema_api.model.TestModel;
import org.mapstruct.Mapping;
import org.mapstruct.Mapper;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface TestMapper {

    @Mapping(target = "normalizedName", source = "name", qualifiedByName = "normalizeName")
    @Mapping(target = "emailDomain", source = "email", qualifiedByName = "extractEmailDomain")
    @Mapping(target = "nameLength", source = "name", qualifiedByName = "nameLength")
    @Mapping(target = "messageLength", source = "message", qualifiedByName = "messageLength")
    @Mapping(target = "hasMessage", source = "message", qualifiedByName = "hasMessage")
    TestModel toModel(TestRequest request);

    @Named("normalizeName")
    default String normalizeName(String name) {
        return name == null ? null : name.trim();
    }

    @Named("extractEmailDomain")
    default String extractEmailDomain(String email) {
        if (email == null) {
            return null;
        }

        int atIndex = email.lastIndexOf('@');
        if (atIndex < 0 || atIndex == email.length() - 1) {
            return null;
        }

        return email.substring(atIndex + 1).toLowerCase();
    }

    @Named("nameLength")
    default Integer nameLength(String name) {
        return name == null ? 0 : name.trim().length();
    }

    @Named("messageLength")
    default Integer messageLength(String message) {
        return message == null ? 0 : message.length();
    }

    @Named("hasMessage")
    default Boolean hasMessage(String message) {
        return message != null && !message.isBlank();
    }
}