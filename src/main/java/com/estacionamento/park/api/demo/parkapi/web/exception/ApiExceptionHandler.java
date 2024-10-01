package com.estacionamento.park.api.demo.parkapi.web.exception;

import com.estacionamento.park.api.demo.parkapi.exception.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErroMessage> accessDeniedException(AccessDeniedException ex, HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).contentType(MediaType.APPLICATION_JSON)
                .body(new ErroMessage(request, HttpStatus.FORBIDDEN,ex.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErroMessage> methodArgumentNotValidException(MethodArgumentNotValidException ex
    , HttpServletRequest request, BindingResult result) {
        log.error("Api Error - ", ex);
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).contentType(MediaType.APPLICATION_JSON)
                .body(new ErroMessage(request, HttpStatus.UNPROCESSABLE_ENTITY, "Campo(s) invalidos", result));
    }

    @ExceptionHandler({UsernameUniqueViolationException.class, CpfUniqueViolationException.class, CodigoUniqueViolationException.class})
    public ResponseEntity<ErroMessage> uniqueViolationException(RuntimeException ex, HttpServletRequest request) {
        log.error("Api Error - ", ex);
        return ResponseEntity.status(HttpStatus.CONFLICT).contentType(MediaType.APPLICATION_JSON).body(new ErroMessage(request, HttpStatus.CONFLICT
                ,ex.getMessage()));
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErroMessage> entityNotFoundException(RuntimeException ex, HttpServletRequest request) {
        log.error("Api Error - ", ex);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).contentType(MediaType.APPLICATION_JSON).body(new ErroMessage(request, HttpStatus.NOT_FOUND
                ,ex.getMessage()));
    }

    @ExceptionHandler(PasswordInvalidException.class)
    public ResponseEntity<ErroMessage> passwordInvalidException(RuntimeException ex, HttpServletRequest request) {
        log.error("Api Error - ", ex);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).contentType(MediaType.APPLICATION_JSON)
                .body(new ErroMessage(request, HttpStatus.BAD_REQUEST,ex.getMessage()));
    }


}
