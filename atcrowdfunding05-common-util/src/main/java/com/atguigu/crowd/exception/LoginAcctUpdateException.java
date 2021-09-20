package com.atguigu.crowd.exception;

public class LoginAcctUpdateException extends RuntimeException{
    private static final long serialVersionUID = 1L;

    public LoginAcctUpdateException() {
        super();
    }

    public LoginAcctUpdateException(String message) {
        super(message);
    }

    public LoginAcctUpdateException(String message, Throwable cause) {
        super(message, cause);
    }

    public LoginAcctUpdateException(Throwable cause) {
        super(cause);
    }

    protected LoginAcctUpdateException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
