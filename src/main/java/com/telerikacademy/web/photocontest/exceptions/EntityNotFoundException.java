package com.telerikacademy.web.photocontest.exceptions;

public class EntityNotFoundException extends RuntimeException {
    public EntityNotFoundException() {
    }

    public EntityNotFoundException(String message) {
        super(message);
    }

    public EntityNotFoundException(String type, long id) {
        this(type, "id", String.valueOf(id));
    }

    public EntityNotFoundException(String type, String attribute, String value) {
        super(String.format("%s with %s %s not found!", type, attribute, value));
    }
    public EntityNotFoundException(String type, long typeId, String attribute, long attributeId) {
        super(String.format("%s with id %s does not contain %s with id %s!", type, typeId, attribute, attributeId));
    }
}
