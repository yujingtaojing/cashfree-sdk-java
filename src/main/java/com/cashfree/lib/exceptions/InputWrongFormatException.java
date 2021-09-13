package com.cashfree.lib.exceptions;

public class InputWrongFormatException extends RuntimeException{
    public InputWrongFormatException() { super(""); }

    public InputWrongFormatException(String str) {
        super(str);
    }
}
