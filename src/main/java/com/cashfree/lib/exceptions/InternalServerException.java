package com.cashfree.lib.exceptions;

public class InternalServerException extends RuntimeException{
    public InternalServerException() { super(""); }

    public InternalServerException(String str) {
        super(str);
    }
}
