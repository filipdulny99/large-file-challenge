package com.kyotu.largefilereadchallenge.exception;

import lombok.Getter;

@Getter
public class FileReadException extends RuntimeException {

    public FileReadException(String message) {
        super(message);
    }
}
