package com.cashfree.lib.exceptions;

public class ConfigMissingForSignatureAuthException extends RuntimeException{
    public ConfigMissingForSignatureAuthException() { super(""); }

    public ConfigMissingForSignatureAuthException(String str) {
        super(str);
    }
}
