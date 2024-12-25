package com.packt.modern.api.exceptions;

public enum ErrorCode {
  GENERIC_ERROR("PACKT-0001", "The system is unable to complete the request. Contact system support"),
  HTTP_MEDIATYPE_NOT_SUPPORTED("PACKT-0002", "Requested media type is not supported. Please use application/json or application/xml as 'Content-Type' header value"),
  HTTP_MESSAGE_NOT_WRITABLE("PACKT-0003", "Missing 'Accept' header. Please add 'Accept' header"),
  HTTP_MEDIA_TYPE_NOT_ACCEPTABLE("PACKT-0004",
      """
          Requested 'Accept' header value is not supported. 
          Please use application/json or application/xml as 'Accept' value
          """
  ),
  JSON_PARSE_ERROR("PACKT-0005",
      """
          Make sure request payload should be a valid JSON object.
          """),
  HTTP_MESSAGE_NOT_READABLE("PACKT-0006",
      """
           Make sure request payload should be a valid JSON or XML\s
           object according to 'Content-Type'.
          """),
  RESOURCE_NOT_FOUND("PACKT-0010", "Requested resource not found"),
  CUSTOMER_NOT_FOUND("PACKT-0011", "Requested customer not found"),
  GENERIC_ALREADY_EXISTS("PACKT-0012", "Already exists"),
  ITEM_NOT_FOUND("PACKT-0013", "Requested item not found"),
  ILLEGAL_ARGUMENT_EXCEPTION("PACKT-0014", "Invalid data passed."),
  CONSTRAINT_VIOLATION("PACKT-0015", "Validation failed."),
  CARD_ALREADY_EXISTS("PACKT-0016", "Card already exists"),
  GENERIC_STATUS_ERROR("PACKT-0017", "Status error." ),
  ADDRESS_NOT_FOUND("PACKT-0018", "Requested address not found"),
  USER_NOT_IN_ADDRESS("PACKT-0019", "User not in address");


  private final String errCode;
  private final String errMsgKey;

  ErrorCode(final String errCode, final String errMsgKey) {
    this.errCode = errCode;
    this.errMsgKey = errMsgKey;
  }

  public String getErrCode() {
    return errCode;
  }

  public String getErrMsgKey() {
    return errMsgKey;
  }
}
