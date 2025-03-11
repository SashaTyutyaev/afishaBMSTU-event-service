package ru.afishaBMSTU.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RestControllerAdvice
@Slf4j
public class ErrorHandler {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @ExceptionHandler(IncorrectParameterException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleIncorrectParameterException(IncorrectParameterException e) {
        log.error("Handle IncorrectParameterException: {}", e.getMessage());
        return new ErrorResponse("Incorrect parameter", HttpStatus.BAD_REQUEST,
                e.getMessage(), LocalDateTime.now().format(FORMATTER));
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleNotFoundException(NotFoundException e) {
        log.error("Handle NotFoundException: {}", e.getMessage());
        return new ErrorResponse("Not found", HttpStatus.NOT_FOUND,
                e.getMessage(), LocalDateTime.now().format(FORMATTER));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleValidationException(MethodArgumentNotValidException e) {
        log.error("Handling validation exception", e);
        return new ErrorResponse("Incorrect request", HttpStatus.BAD_REQUEST,
                e.getMessage(), LocalDateTime.now().format(FORMATTER));
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleDataIntegrityViolationException(DataIntegrityViolationException e) {
        log.error("Handling data integrity violation", e);
        return new ErrorResponse("Incorrect request", HttpStatus.CONFLICT,
                e.getMessage(), LocalDateTime.now().format(FORMATTER));
    }
}
