package hr.fer.caloriecounter.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
public class CustomExceptionHandler {
    @ExceptionHandler(EmailExistsException.class)
    public ResponseEntity<ErrorResponse> handleEmailExistsException(HttpServletRequest request, Exception e) {
        return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage(), "email"));
    }

    @ExceptionHandler(FoodExistsException.class)
    public ResponseEntity<ErrorResponse> handleFoodExistsException(HttpServletRequest request, Exception e) {
        return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage(), "food"));
    }

    @ExceptionHandler(MealExistsException.class)
    public ResponseEntity<ErrorResponse> handleMealExistsException(HttpServletRequest request, Exception e) {
        return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage(), "meal"));
    }

    @ExceptionHandler(ProgressExistsException.class)
    public ResponseEntity<ErrorResponse> handleProgressExistsException(HttpServletRequest request, Exception e) {
        return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage(), "progress"));
    }

    @ExceptionHandler(UsernameExistsException.class)
    public ResponseEntity<ErrorResponse> handleUsernameExistsException(HttpServletRequest request, Exception e) {
        return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage(), "username or password"));
    }

    @ExceptionHandler(FoodNotFound.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Food not found")
    public void handleFoodNotFoundException(HttpServletRequest request, Exception e) {
    }

    @ExceptionHandler(ImageNotFound.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Image not found")
    public void handleImageNotFoundException(HttpServletRequest request, Exception e) {
    }

    @ExceptionHandler(MealNotFound.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Meal not found")
    public void handleMealNotFoundException(HttpServletRequest request, Exception e) {
    }

    @ExceptionHandler(ProgressNotFound.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Progress not found")
    public void handleProgressNotFoundException(HttpServletRequest request, Exception e) {
    }

    @ExceptionHandler(IncorrectPassswordException.class)
    public ResponseEntity<ErrorResponse> handleIncorrectPasswordException(HttpServletRequest request, Exception e) {
        return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage(), "username or password"));
    }
}
