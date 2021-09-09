package com.cashfree.lib.exceptions;

public class EntityDoesntExistException extends RuntimeException{
    public EntityDoesntExistException() { super(""); }

    public EntityDoesntExistException(String str) {
        super(str);
    }
}

