package com.packt.modern.api.exceptions;

import java.io.Serial;

public class CustomerNotFoundException extends RuntimeException {
  @Serial
  private static final long serialVersionUID = 1L;
  private final String errMsgKey;
  private final String errorCode;

  public CustomerNotFoundException(final String format) {
    super(format);
    this.errMsgKey = ErrorCode.CUSTOMER_NOT_FOUND.getErrMsgKey();
    this.errorCode = ErrorCode.CUSTOMER_NOT_FOUND.getErrCode();
  }

 public CustomerNotFoundException(final ErrorCode errorCode) {
    super(errorCode.getErrMsgKey());
    this.errMsgKey = errorCode.getErrMsgKey();
    this.errorCode = errorCode.getErrCode();
 }

  public String getErrMsgKey() {
    return errMsgKey;
  }

  public String getErrorCode() {
    return errorCode;
  }
}
