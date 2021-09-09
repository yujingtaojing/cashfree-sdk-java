package com.cashfree.lib.exceptions;

public class TooManyRequestException extends RuntimeException{
    public TooManyRequestException() { super(""); }

    public TooManyRequestException(String str) {
        super(str);
    }
}
