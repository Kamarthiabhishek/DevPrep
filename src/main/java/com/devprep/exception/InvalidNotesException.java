package com.devprep.exception;

public class InvalidNotesException extends RuntimeException{
    public InvalidNotesException(String message){
        super(message);
    }
}
