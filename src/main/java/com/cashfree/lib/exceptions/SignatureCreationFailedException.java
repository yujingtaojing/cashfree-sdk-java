package com.cashfree.lib.exceptions;

public class SignatureCreationFailedException extends RuntimeException{
    public SignatureCreationFailedException() { super(""); }

    public SignatureCreationFailedException(String str) {
        super(str);
    }
}

