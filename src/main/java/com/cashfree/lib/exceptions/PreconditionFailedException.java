package com.cashfree.lib.exceptions;

public class PreconditionFailedException extends RuntimeException{
    public PreconditionFailedException() { super(""); }

    public PreconditionFailedException(String str) {
        super(str);
    }
}
