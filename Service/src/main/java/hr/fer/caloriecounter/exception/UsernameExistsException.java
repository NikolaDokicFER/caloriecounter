package hr.fer.caloriecounter.exception;

public class UsernameExistsException extends RuntimeException{
    public UsernameExistsException(String exceptionMessage){
        super(exceptionMessage);
    }
}
