package ru.practicum.ewm.base.exceptions.ErrorBase;

import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.ewm.base.exceptions.*;

@Slf4j
@RestControllerAdvice
public class ErrorHandler {
    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleConflictException(final ConflictException e) {
        log.error(HttpStatus.CONFLICT.getReasonPhrase(), e.getLocalizedMessage(), e.getMessage());
        return new ErrorResponse(
                HttpStatus.CONFLICT.toString(),
                "ConflictException",
                e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleIntegrityException(final IntegrityException e) {
        log.error(HttpStatus.CONFLICT.getReasonPhrase(), e.getLocalizedMessage(), e.getMessage());
        return new ErrorResponse(
                HttpStatus.CONFLICT.toString(),
                "IntegrityException",
                e.getMessage());
    }


    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleDataIntegrityViolationException(final DataIntegrityViolationException e) {
        log.error(HttpStatus.CONFLICT.getReasonPhrase(), e.getLocalizedMessage(), e.getMessage());
        return new ErrorResponse(
                HttpStatus.CONFLICT.toString(),
                "DataIntegrityViolationException",
                e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleDataNotFoundException(final DataNotFoundException e) {
        log.error(HttpStatus.NOT_FOUND.toString(), e.getLocalizedMessage(), e.getMessage());
        return new ErrorResponse(
                HttpStatus.NOT_FOUND.toString(),
                "DataNotFoundException",
                e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleBadRequestException(final BadRequestException e) {
        log.error(HttpStatus.BAD_REQUEST.toString(), e.getLocalizedMessage(), e.getMessage());
        return new ErrorResponse(
                HttpStatus.BAD_REQUEST.toString(),
                "BadRequestException",
                e.getMessage());
    }


    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse validationMethodArgumentNotValidException(final MethodArgumentNotValidException e) {
        log.error(HttpStatus.BAD_REQUEST.toString(), e.getLocalizedMessage(), e.getMessage());
        return new ErrorResponse(
                HttpStatus.BAD_REQUEST.toString(),
                "MethodArgumentNotValidException",
                e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleConstraintViolationException(final ConstraintViolationException e) {
        log.error(HttpStatus.BAD_REQUEST.toString(), e.getLocalizedMessage(), e.getMessage());
        return new ErrorResponse(
                HttpStatus.BAD_REQUEST.toString(),
                "ConstraintViolationException",
                e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleThrowable(final Throwable e) {
        log.error(HttpStatus.INTERNAL_SERVER_ERROR.toString(), e.getLocalizedMessage(), e.getMessage());
        return new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.toString(),
                "Throwable",
                e.getMessage());
    }
}

