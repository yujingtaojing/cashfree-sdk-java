package com.cashfree.lib.exceptions;

public class CredsNotPresentException extends RuntimeException{
    public CredsNotPresentException() { super(""); }

    public CredsNotPresentException(String str) {
        super(str);
    }
}
