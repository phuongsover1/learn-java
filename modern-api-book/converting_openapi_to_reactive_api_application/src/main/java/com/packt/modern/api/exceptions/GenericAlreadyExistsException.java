package com.packt.modern.api.exceptions;

public class GenericAlreadyExistsException extends RuntimeException {
  private final String errMgsKey;
  private final String errorCode;
  public GenericAlreadyExistsException(final String message) {
    super(message);
    this.errMgsKey = ErrorCode.GENERIC_ALREADY_EXISTS.getErrMsgKey();
    this.errorCode = ErrorCode.GENERIC_ALREADY_EXISTS.getErrCode();
  }

  public String getErrMgsKey() {
    return errMgsKey;
  }

  public String getErrorCode() {
    return errorCode;
  }
}
