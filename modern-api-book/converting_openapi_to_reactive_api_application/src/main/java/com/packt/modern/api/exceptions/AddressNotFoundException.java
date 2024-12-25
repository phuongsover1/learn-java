package com.packt.modern.api.exceptions;

import java.io.Serial;

public class AddressNotFoundException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 1L;
    private final String errMsgKey;
    private final String errorCode;

    public AddressNotFoundException(final String format) {
        super(format);
        this.errMsgKey = ErrorCode.ADDRESS_NOT_FOUND.getErrCode();
        this.errorCode = ErrorCode.ADDRESS_NOT_FOUND.getErrMsgKey();
    }

    public AddressNotFoundException(final ErrorCode errorCode) {
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
