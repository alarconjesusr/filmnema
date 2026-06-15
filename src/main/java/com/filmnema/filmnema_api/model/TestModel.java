package com.filmnema.filmnema_api.model;

public record TestModel(
        String name,
        String email,
        String message,
        String normalizedName,
        String emailDomain,
        Integer nameLength,
        Integer messageLength,
        Boolean hasMessage
) {
}