package org.dci.aimealplanner.exceptions;

public class EmailAlreadyTaken extends RuntimeException {
    public EmailAlreadyTaken(String message) {
        super(message);
    }
}
