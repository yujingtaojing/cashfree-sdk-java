package com.cashfree.lib.exceptions;

public class ModuleNotInitiatedException extends RuntimeException{
    public ModuleNotInitiatedException() { super(""); }

    public ModuleNotInitiatedException(String str) {
        super(str);
    }
}

