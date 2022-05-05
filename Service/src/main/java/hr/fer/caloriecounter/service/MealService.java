package hr.fer.caloriecounter.service;

import hr.fer.caloriecounter.model.Meal;
import hr.fer.caloriecounter.model.Progress;
import hr.fer.caloriecounter.repository.MealRepo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Date;

@Service
@AllArgsConstructor
public class MealService {
    private final MealRepo mealRepository;

    public Meal saveMeal(Meal meal){
        if(this.mealRepository.existsByUserIdAndDate(meal.getUserId(), meal.getDate())){
            System.out.println("Postoji vec");
            return null;
        }else{
            return this.mealRepository.save(meal);
        }
    }

    public Meal getMeal(Long userId, Date date) throws Exception{
        return this.mealRepository.getByUserIdAndDate(userId, date).orElseThrow(Exception::new);
    }
}
