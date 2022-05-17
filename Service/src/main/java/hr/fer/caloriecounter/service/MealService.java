package hr.fer.caloriecounter.service;

import hr.fer.caloriecounter.exception.IncorrectMealException;
import hr.fer.caloriecounter.exception.MealExistsException;
import hr.fer.caloriecounter.model.Meal;
import hr.fer.caloriecounter.model.User;
import hr.fer.caloriecounter.repository.MealRepo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

@Service
@AllArgsConstructor
public class MealService {
    private final MealRepo mealRepository;

    public Meal saveMeal(Meal meal){
        if(this.mealRepository.existsByUserAndDate(meal.getUser(), meal.getDate())){
            throw new MealExistsException("Meal already exists");
        }else{
            return this.mealRepository.save(meal);
        }
    }

    public List<Meal> getMeal(User user, LocalDate date){
        return this.mealRepository.getByUserAndDate(user, date).orElseThrow(() ->
                new IncorrectMealException("Meal not found"));
    }
}
