package com.cashfree.lib.exceptions;

public class AlreadyExistException extends RuntimeException{
    public AlreadyExistException() { super(""); }

    public AlreadyExistException(String str) {
        super(str);
    }
}

