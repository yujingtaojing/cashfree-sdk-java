package com.cashfree.lib.exceptions;

public class AuthenticationFailureException extends RuntimeException{
    public AuthenticationFailureException() { super(""); }

    public AuthenticationFailureException(String str) {
        super(str);
    }
}
