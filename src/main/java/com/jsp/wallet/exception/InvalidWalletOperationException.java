package com.jsp.wallet.exception;

public class InvalidWalletOperationException extends RuntimeException {

    public InvalidWalletOperationException(String message) {
        super(message);
    }
}