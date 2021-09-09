package com.cashfree.lib.exceptions;

public class TokenGenFailedException extends RuntimeException{
    public TokenGenFailedException() { super(""); }

    public TokenGenFailedException(String str) {
        super(str);
    }
}
