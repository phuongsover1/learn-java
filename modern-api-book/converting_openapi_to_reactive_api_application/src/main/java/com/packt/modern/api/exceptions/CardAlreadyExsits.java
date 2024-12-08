package com.packt.modern.api.exceptions;

public class CardAlreadyExsits extends RuntimeException {
  private static final long serialVersionUID = 1L;
  private final String errMsgKey;
  private final String errorCode;

  public CardAlreadyExsits(ErrorCode errorCode) {
    super(errorCode.getErrMsgKey());
    this.errMsgKey = errorCode.getErrMsgKey();
    this.errorCode = errorCode.getErrCode();
  }

  public CardAlreadyExsits(String message) {
    super(message);
    errorCode = ErrorCode.CARD_ALREADY_EXISTS.getErrCode();
    errMsgKey = ErrorCode.CARD_ALREADY_EXISTS.getErrMsgKey();
  }

  public String getErrorCode() {
    return errorCode;
  }

  public String getErrMsgKey() {
    return errMsgKey;
  }
}
