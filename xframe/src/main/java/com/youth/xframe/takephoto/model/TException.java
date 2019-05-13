package com.youth.xframe.takephoto.model;

/**
 */
public class TException extends Exception {
    String detailMessage;

    public TException(TExceptionType exceptionType) {
        super(exceptionType.getStringValue());
        this.detailMessage = exceptionType.getStringValue();
    }

    public String getDetailMessage() {
        return detailMessage;
    }
}
