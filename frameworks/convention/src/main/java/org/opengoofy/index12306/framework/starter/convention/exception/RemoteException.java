package org.opengoofy.index12306.framework.starter.convention.exception;

import org.opengoofy.index12306.framework.starter.convention.errorcode.BaseErrorCode;
import org.opengoofy.index12306.framework.starter.convention.errorcode.IErrorCode;

public class RemoteException extends  AbstractException{
    public RemoteException(String message, Throwable throwable, IErrorCode errorCode) {
        super(message, throwable, errorCode);
    }
    public RemoteException(String message) {
        super(message, null, BaseErrorCode.REMOTE_ERROR);
    }
    public RemoteException(IErrorCode errorCode) {
        super(null, null, errorCode);
    }
    public RemoteException(String message, IErrorCode errorCode) {
        super(message, null, errorCode);
    }

    @Override
    public String toString() {
        return "RemoteException{" +
                "errorCode='" + errorCode + '\'' +
                ", errorMessage='" + errorMessage + '\'' +
                '}';
    }
}
