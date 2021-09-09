package com.cashfree.lib.exceptions;

public class ForbiddenException extends RuntimeException{
    public ForbiddenException() { super(""); }

    public ForbiddenException(String str) {
        super(str);
    }
}
