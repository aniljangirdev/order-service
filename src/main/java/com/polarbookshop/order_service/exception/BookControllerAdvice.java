package com.polarbookshop.order_service.exception;

import com.polarbookshop.order_service.domain.Error;
import jakarta.validation.UnexpectedTypeException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDate;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class BookControllerAdvice {

    @ExceptionHandler(BookNotFoundException.class)
    public ResponseEntity<Error> handleNotFoundException(BookNotFoundException exception) {
        var error = new Error(
                exception.getMessage(),
                LocalDate.now()
        );
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(BookAlreadyExistsException.class)
    public ResponseEntity<Error> handleBookAlreadyExistException(BookAlreadyExistsException exception) {
        var error = new Error(
                exception.getMessage(),
                LocalDate.now()
        );
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<Error> handleGlobalException(Exception exception) {
        var error = new Error(
                exception.getMessage(),
                LocalDate.now()
        );
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(UnexpectedTypeException.class)
    public ResponseEntity<Error> handleUnexpectedTypeException(UnexpectedTypeException exception) {
        var error = new Error(
                exception.getMessage(),
                LocalDate.now()
        );
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ResponseEntity<Error> handleMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
        Map<String, String> errorMap =
                exception
                        .getBindingResult()
                        .getAllErrors()
                        .stream()
                        .collect(Collectors.toMap(
                                objectError -> ((FieldError) objectError).getField(),
                                ObjectError::getDefaultMessage
                        ));

        var error = new Error(
                errorMap,
                LocalDate.now()
        );

        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }
}
