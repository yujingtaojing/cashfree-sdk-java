package com.cashfree.lib.exceptions;

public class IncompleteParametersException extends RuntimeException{
    public IncompleteParametersException() { super(""); }

    public IncompleteParametersException(String str) {
        super(str);
    }
}
