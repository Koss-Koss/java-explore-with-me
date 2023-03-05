package ru.practicum.ewmmainservice.exception;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.util.Date;

import static org.springframework.http.HttpStatus.*;

@RestControllerAdvice
@Slf4j
public class ErrorHandler {
    @ExceptionHandler(value = {NotFoundException.class})
    @ResponseStatus(NOT_FOUND)
    public ErrorMessage handleNotFoundException(Exception exception, WebRequest request) {
        int statusCode = NOT_FOUND.value();
        ErrorMessage errorMessage =
                new ErrorMessage(new Date(), statusCode, exception.getMessage(), request.getDescription(false));
        log.info("Ошибка запроса {} - {}", statusCode, exception.getMessage());
        return errorMessage;
    }

    @ExceptionHandler(value = {ConflictException.class})
    @ResponseStatus(CONFLICT)
    public ErrorMessage handleConflictException(Exception exception, WebRequest request) {
        int statusCode = CONFLICT.value();
        ErrorMessage errorMessage =
                new ErrorMessage(new Date(), statusCode, exception.getMessage(), request.getDescription(false));
        log.info("Ошибка запроса {} - {}", statusCode, exception.getMessage());
        return errorMessage;
    }

    @ExceptionHandler(value = {BadRequestException.class})
    @ResponseStatus(BAD_REQUEST)
    public ErrorMessage handleBadRequestException(Exception exception, WebRequest request) {
        int statusCode = BAD_REQUEST.value();
        ErrorMessage errorMessage =
                new ErrorMessage(new Date(), statusCode, exception.getMessage(), request.getDescription(false));
        log.info("Ошибка запроса {} - {}", statusCode, exception.getMessage());
        return errorMessage;
    }

    @ExceptionHandler(value = {MethodArgumentNotValidException.class})
    @ResponseStatus(BAD_REQUEST)
    public ErrorMessage handleMethodArgumentNotValidException(
            MethodArgumentNotValidException exception, WebRequest request) {
        int statusCode = BAD_REQUEST.value();
        ErrorMessage errorMessage =
                new ErrorMessage(new Date(), statusCode, exception.getMessage(), request.getDescription(false));
        log.info("Ошибка запроса {} - {}", statusCode, exception.getMessage());
        return errorMessage;
    }

    @ExceptionHandler(value = {MissingServletRequestParameterException.class})
    @ResponseStatus(BAD_REQUEST)
    public ErrorMessage handleMissingServletRequestParameterException(
            MissingServletRequestParameterException exception, WebRequest request) {
        int statusCode = BAD_REQUEST.value();
        ErrorMessage errorMessage =
                new ErrorMessage(new Date(), statusCode, exception.getMessage(), request.getDescription(false));
        log.info("Ошибка запроса {} - {}", statusCode, exception.getMessage());
        return errorMessage;
    }

    @ExceptionHandler(value = {DataIntegrityViolationException.class})
    @ResponseStatus(CONFLICT)
    public ErrorMessage handleDataIntegrityViolationException(
            DataIntegrityViolationException exception, WebRequest request) {
        int statusCode = CONFLICT.value();
        ErrorMessage errorMessage =
                new ErrorMessage(new Date(), statusCode, exception.getMessage(), request.getDescription(false));
        log.info("Ошибка запроса {} - {}", statusCode, exception.getMessage());
        return errorMessage;
    }

    @ExceptionHandler(value = {ConstraintViolationException.class})
    @ResponseStatus(CONFLICT)
    public ErrorMessage handleConstraintViolationException(
            ConstraintViolationException exception, WebRequest request) {
        int statusCode = CONFLICT.value();
        ErrorMessage errorMessage =
                new ErrorMessage(new Date(), statusCode, exception.getSQLException().getMessage(),
                        request.getDescription(false));
        log.info("Ошибка запроса {} - {}", statusCode, exception.getMessage());
        return errorMessage;
    }

    @ExceptionHandler(Throwable.class)
    public ResponseEntity<Object> handleThrowable() {
        log.info("Непредвиденная ошибка обработки запроса (status code 500)");
        return new ResponseEntity<>("Не удается обработать запрос", INTERNAL_SERVER_ERROR);
    }

}
