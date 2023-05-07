package com.capedbaldy.braindumpstr.errors;

import org.springframework.stereotype.Component;


public class IllegalArgumentException extends RuntimeException {
    public IllegalArgumentException(String message) {
        super(message);
    }
}
