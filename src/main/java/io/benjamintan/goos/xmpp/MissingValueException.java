package io.benjamintan.goos.xmpp;

public class MissingValueException extends Exception {
    public MissingValueException(String fieldName) {
        super(fieldName);
    }
}
