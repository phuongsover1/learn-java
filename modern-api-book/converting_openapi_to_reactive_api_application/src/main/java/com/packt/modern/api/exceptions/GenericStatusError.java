package com.packt.modern.api.exceptions;

public class GenericStatusError extends RuntimeException {
  private static final long serialVersionUID = 1L;
  private final String errorCode;
  private final String errorMsg;

  public GenericStatusError(String message) {
    super(message);
    errorCode = ErrorCode.GENERIC_STATUS_ERROR.getErrCode();
    errorMsg = ErrorCode.GENERIC_STATUS_ERROR.getErrMsgKey();
  }

  public GenericStatusError(ErrorCode errorCode) {
    super(errorCode.getErrMsgKey());
    this.errorCode = errorCode.getErrCode();
    this.errorMsg = errorCode.getErrMsgKey();
  }
}
