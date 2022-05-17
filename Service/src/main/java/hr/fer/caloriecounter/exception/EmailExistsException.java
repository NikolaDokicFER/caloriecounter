package hr.fer.caloriecounter.exception;

public class EmailExistsException extends RuntimeException{
    public EmailExistsException(String exceptionMessage){
        super(exceptionMessage);
    }
}
