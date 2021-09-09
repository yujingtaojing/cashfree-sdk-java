package com.cashfree.lib.exceptions;

public class RequestTooLargeException extends RuntimeException{
    public RequestTooLargeException() { super(""); }

    public RequestTooLargeException(String str) {
        super(str);
    }
}
