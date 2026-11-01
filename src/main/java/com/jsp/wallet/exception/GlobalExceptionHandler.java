package com.jsp.wallet.exception;

import java.util.HashMap;
import com.jsp.wallet.exception.WalletNotFoundException;
import com.jsp.wallet.exception.InsufficientBalanceException;
import java.util.Map;
import org.springframework.security.access.AccessDeniedException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationErrors(
            MethodArgumentNotValidException ex) {

        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getFieldErrors().forEach(error -> {
            errors.put(error.getField(), error.getDefaultMessage());
        });

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }

	@ExceptionHandler(EmailAlreadyExistsException.class)
	public ResponseEntity<Map<String, String>> handleDuplicateEmail(
	        EmailAlreadyExistsException ex) {

	    Map<String, String> error = new HashMap<>();
	    error.put("error", ex.getMessage());

	    return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
	}
	
	@ExceptionHandler(InvalidCredentialsException.class)
	public ResponseEntity<Map<String, String>> handleInvalidCredentials(
	        InvalidCredentialsException ex) {

	    Map<String, String> error = new HashMap<>();
	    error.put("error", ex.getMessage());

	    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
	}
	
	@ExceptionHandler(WalletNotFoundException.class)
	public ResponseEntity<Map<String, String>> handleWalletNotFound(
	        WalletNotFoundException ex) {

	    Map<String, String> error = new HashMap<>();
	    error.put("error", ex.getMessage());

	    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
	}

	@ExceptionHandler(InsufficientBalanceException.class)
	public ResponseEntity<Map<String, String>> handleInsufficientBalance(
	        InsufficientBalanceException ex) {

	    Map<String, String> error = new HashMap<>();
	    error.put("error", ex.getMessage());

	    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
	}
	
	@ExceptionHandler(InvalidWalletOperationException.class)
	public ResponseEntity<Map<String, String>> handleInvalidWalletOperation(
	        InvalidWalletOperationException ex) {

	    Map<String, String> error = new HashMap<>();
	    error.put("error", ex.getMessage());

	    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
	}
	
	@ExceptionHandler(AccessDeniedException.class)
	public ResponseEntity<Map<String, String>> handleAccessDenied(
	        AccessDeniedException ex) {

	    Map<String, String> error = new HashMap<>();
	    error.put("error", ex.getMessage());

	    return ResponseEntity
	            .status(HttpStatus.FORBIDDEN)
	            .body(error);
	}
}