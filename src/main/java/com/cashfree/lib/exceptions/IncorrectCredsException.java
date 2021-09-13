package com.cashfree.lib.exceptions;

public class IncorrectCredsException extends RuntimeException{
    public IncorrectCredsException() { super(""); }

    public IncorrectCredsException(String str) {
        super(str);
    }
}
