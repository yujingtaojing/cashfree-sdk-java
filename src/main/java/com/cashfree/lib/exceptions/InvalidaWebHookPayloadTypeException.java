package com.cashfree.lib.exceptions;

public class InvalidaWebHookPayloadTypeException extends RuntimeException{
    public InvalidaWebHookPayloadTypeException() { super(""); }

    public InvalidaWebHookPayloadTypeException(String str) {
        super(str);
    }
}
