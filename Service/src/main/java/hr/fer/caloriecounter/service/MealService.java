package hr.fer.caloriecounter.service;

import hr.fer.caloriecounter.exception.MealNotFound;
import hr.fer.caloriecounter.exception.MealExistsException;
import hr.fer.caloriecounter.model.Meal;
import hr.fer.caloriecounter.model.User;
import hr.fer.caloriecounter.repository.MealRepo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@AllArgsConstructor
public class MealService {
    private final MealRepo mealRepository;

    public Meal saveMeal(Meal meal){
        if(this.mealRepository.existsByUserIdAndFoodIdAndDateAndType(meal.getUser().getId(), meal.getFood().getId(), meal.getDate(), meal.getType())){
            Meal existingMeal = this.mealRepository.getByUserIdAndFoodIdAndDateAndType(meal.getUser().getId(), meal.getFood().getId(), meal.getDate(), meal.getType());
            existingMeal.setQuantity(existingMeal.getQuantity() + meal.getQuantity());
            return this.mealRepository.save(existingMeal);
        }else {
            return this.mealRepository.save(meal);
        }
    }

    public List<Meal> getMeal(User user, LocalDate date){
        return this.mealRepository.getByUserAndDate(user, date).orElseThrow(() ->
                new MealNotFound("Meal not found"));
    }

    public void deleteMeal(Long mealId){
        if(!this.mealRepository.existsById(mealId)){
            throw new MealExistsException("Meal does not exist");
        }else{
            this.mealRepository.deleteById(mealId);
        }
    }
}
