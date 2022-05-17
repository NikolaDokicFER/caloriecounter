package hr.fer.caloriecounter.exception;

public class IncorrectFoodException extends RuntimeException{
    public IncorrectFoodException(String exceptionMessage){
        super(exceptionMessage);
    }
}
