package com.packt.modern.api.exceptions;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Locale;

@ControllerAdvice
public class RestApiErrorHandler {
  private final MessageSource messageSource;

  public RestApiErrorHandler(MessageSource messageSource) {
    this.messageSource = messageSource;
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<Error> handleException(HttpServletRequest request, Exception exception, Locale locale) {
    Error error = ErrorUtils.createError(ErrorCode.GENERIC_ERROR.getErrMsgKey(),
        ErrorCode.GENERIC_ERROR.getErrCode(),
        HttpStatus.INTERNAL_SERVER_ERROR.value());
    error.setUrl(request.getRequestURL().toString());
    error.setReqMethod(request.getMethod());
    return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
  }

  @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
  public ResponseEntity<Error> handleHttpMediaTypeNotSupportedException(HttpServletRequest request, HttpMediaTypeNotSupportedException exception, Locale locale) {
    Error error = ErrorUtils.createError(ErrorCode.HTTP_MEDIA_TYPE_NOT_ACCEPTABLE.getErrMsgKey(),
        ErrorCode.HTTP_MEDIATYPE_NOT_SUPPORTED.getErrCode(),
        HttpStatus.UNSUPPORTED_MEDIA_TYPE.value());
    error.setUrl(request.getRequestURL().toString());
    error.setReqMethod(request.getMethod());
    return new ResponseEntity<>(error, HttpStatus.UNSUPPORTED_MEDIA_TYPE);
  }
}
