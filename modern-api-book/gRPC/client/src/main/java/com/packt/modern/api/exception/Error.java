package com.packt.modern.api.exception;

import java.time.Instant;

public class Error {
  private static final long serialVersionUID = 1L;
  private String message;
  /** HTTP status code for this occurrence of the problem, set by the origin server */
  private Integer status;
  private String url;
  private String reqMethod;
  private Instant timestamp;

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public Integer getStatus() {
    return status;
  }

  public void setStatus(Integer status) {
    this.status = status;
  }

  public String getReqMethod() {
    return reqMethod;
  }

  public void setReqMethod(String reqMethod) {
    this.reqMethod = reqMethod;
  }

  public Instant getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(Instant timestamp) {
    this.timestamp = timestamp;
  }
}
