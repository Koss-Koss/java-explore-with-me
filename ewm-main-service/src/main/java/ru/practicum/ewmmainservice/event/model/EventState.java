package ru.practicum.ewmmainservice.event.model;

public enum EventState {
    PENDING,
    PUBLISHED,
    CANCELED,
    PUBLISH_EVENT,
    REJECT_EVENT,
    SEND_TO_REVIEW,
    CANCEL_REVIEW
}
