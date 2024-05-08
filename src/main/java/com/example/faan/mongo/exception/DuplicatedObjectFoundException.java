package com.example.faan.mongo.exception;

public class DuplicatedObjectFoundException extends RuntimeException {

        public DuplicatedObjectFoundException() {
        }

        public DuplicatedObjectFoundException(String message) {
            super(message);
        }

        public DuplicatedObjectFoundException(String message, Throwable cause) {
            super(message, cause);
        }
}
