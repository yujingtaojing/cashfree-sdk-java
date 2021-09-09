package com.cashfree.lib.exceptions;

public class MethodNotAllowedException extends RuntimeException{
    public MethodNotAllowedException() { super(""); }

    public MethodNotAllowedException(String str) {
        super(str);
    }
}
