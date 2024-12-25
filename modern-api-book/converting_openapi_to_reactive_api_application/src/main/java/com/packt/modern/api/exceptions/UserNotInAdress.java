package com.packt.modern.api.exceptions;

import java.io.Serial;

public class UserNotInAdress extends RuntimeException {
  @Serial
  private static final long serialVersionUID = 1L;
  private final String errMsgKey;
  private final String errorCode;

  public UserNotInAdress(final String format) {
    super(format);
    this.errMsgKey = ErrorCode.ADDRESS_NOT_FOUND.getErrCode();
    this.errorCode = ErrorCode.ADDRESS_NOT_FOUND.getErrMsgKey();
  }

  public UserNotInAdress(final ErrorCode errorCode) {
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
