package com.packt.modern.api.exception;

import java.time.Instant;

public class ErrorUtils {
  public static Error createError(Integer status, String message, String reqMethod, String reqUrl, Instant timestamp) {
    Error error = new Error();
    error.setStatus(status);
    error.setMessage(message);
    error.setReqMethod(reqMethod);
    error.setUrl(reqUrl);
    error.setTimestamp(timestamp);
    return error;
  }
}
