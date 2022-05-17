package hr.fer.caloriecounter.exception;

public class IncorrectPassswordException extends RuntimeException{
    public IncorrectPassswordException(String exceptionMessage){
        super(exceptionMessage);
    }
}
