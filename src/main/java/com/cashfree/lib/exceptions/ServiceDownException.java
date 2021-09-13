package com.cashfree.lib.exceptions;

public class ServiceDownException extends RuntimeException{
    public ServiceDownException() { super(""); }

    public ServiceDownException(String str) {
        super(str);
    }
}

