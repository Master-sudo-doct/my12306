package org.opengoofy.index12306.framework.starter.convention.exception;

import org.opengoofy.index12306.framework.starter.convention.errorcode.BaseErrorCode;
import org.opengoofy.index12306.framework.starter.convention.errorcode.IErrorCode;

public class ServiceException extends  AbstractException{
    public ServiceException(String message, Throwable throwable, IErrorCode errorCode) {
        super(message, throwable, errorCode);
    }
    public ServiceException(String message) {
        super(message, null, BaseErrorCode.SERVICE_ERROR);
    }
    public ServiceException(IErrorCode errorCode) {
        super(null, null, errorCode);
    }
    public ServiceException(String message, IErrorCode errorCode) {
        super(message, null, errorCode);
    }

    @Override
    public String toString() {
        return "ServiceException{" +
                "errorCode='" + errorCode + '\'' +
                ", errorMessage='" + errorMessage + '\'' +
                '}';
    }
}
