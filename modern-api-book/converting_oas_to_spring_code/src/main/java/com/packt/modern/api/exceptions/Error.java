package com.packt.modern.api.exceptions;

import java.time.Instant;
import org.apache.logging.log4j.util.Strings;

public class Error {
  private static final long serialVersionUID = 1L;

  /** Application error code, which is different from HTTP error code */
  private String errorCode;

  /** Short, human-readable summary of the problem */
  private String message;

  /** HTTP status code for this occurrence of the problem, set by the origin server */
  private Integer status;

  /** Url of request that produced the error */
  private String url = "Not available";

  /** Method of request that produced the error */
  private String reqMethod = "Not available";

  /** Timestamp */
  private Instant timestamp;

  public String getErrorCode() {
    return errorCode;
  }

  public Error setErrorCode(String errorCode) {
    this.errorCode = errorCode;
    return this;
  }

  public String getMessage() {
    return message;
  }

  public Error setMessage(String message) {
    this.message = message;
    return this;
  }

  public Integer getStatus() {
    return status;
  }

  public Error setStatus(Integer status) {
    this.status = status;
    return this;
  }

  public String getUrl() {
    return url;
  }

  public Error setUrl(String url) {
    if (Strings.isNotBlank(url)) this.url = url;
    return this;
  }

  public String getReqMethod() {
    return reqMethod;
  }

  public Error setReqMethod(String reqMethod) {
    if (Strings.isNotBlank(reqMethod)) this.reqMethod = reqMethod;
    return this;
  }

  public Instant getTimestamp() {
    return timestamp;
  }

  public Error setTimestamp(Instant timestamp) {
    this.timestamp = timestamp;
    return this;
  }
}
