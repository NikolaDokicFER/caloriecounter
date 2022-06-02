package hr.fer.caloriecounter.exception;

public class FoodNotFound extends RuntimeException{
    public FoodNotFound(String exceptionMessage){
        super(exceptionMessage);
    }
}
