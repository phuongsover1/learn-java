package com.packt.modern.api.exception;

import io.grpc.StatusRuntimeException;
import jakarta.servlet.http.HttpServletRequest;
import java.time.Instant;
import java.util.Locale;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class RestApiErrorHandler {
  private final MessageSource messageSource;

  public RestApiErrorHandler(MessageSource messageSource) {
    this.messageSource = messageSource;
  }

  @ExceptionHandler(StatusRuntimeException.class)
  public ResponseEntity<Error> handleInvalidArgumentException(
      HttpServletRequest request, StatusRuntimeException ex, Locale locale) {
    Error error;
    HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;

    switch (ex.getStatus().getCode()) {
      case INVALID_ARGUMENT -> {
          error =
              ErrorUtils.createError(
                  HttpStatus.BAD_REQUEST.value(),
                  ex.getMessage(),
                  request.getMethod(),
                  request.getRequestURL().toString(),
                  Instant.now());
          status = HttpStatus.BAD_REQUEST;
      }
      default -> {
        error =
            ErrorUtils.createError(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                ex.getMessage(),
                request.getMethod(),
                request.getRequestURL().toString(),
                Instant.now());
      }
    }

    return new ResponseEntity<>(error, status);
  }
}
