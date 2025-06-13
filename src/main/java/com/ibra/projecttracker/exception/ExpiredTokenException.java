package com.ibra.projecttracker.exception;
// Add these to your exception package
public class ExpiredTokenException extends RuntimeException {
    public ExpiredTokenException(String message) { super(message); }
}


