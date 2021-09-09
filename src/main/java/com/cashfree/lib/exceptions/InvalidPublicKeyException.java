package com.cashfree.lib.exceptions;

public class InvalidPublicKeyException extends RuntimeException{
    public InvalidPublicKeyException() { super(""); }

    public InvalidPublicKeyException(String str) {
        super(str);
    }
}
