package hr.fer.caloriecounter.controller;

import hr.fer.caloriecounter.model.Meal;
import hr.fer.caloriecounter.model.User;
import hr.fer.caloriecounter.service.MealService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("api/meal")
@CrossOrigin("*")
public class MealController {
    private final MealService mealService;

    @PostMapping
    public Meal saveMeal(@RequestBody Meal meal){
        return this.mealService.saveMeal(meal);
    }

    @GetMapping("{userId}/{date}")
    public List<Meal> getMeal(@PathVariable("userId") User user, @PathVariable String date){
        LocalDate localDate = LocalDate.parse(date);
        return this.mealService.getMeal(user, localDate);
    }

    @GetMapping("{userId}")
    public List<Meal> getMealByUser(@PathVariable("userId") User user){
        return this.mealService.getMealByUser(user);
    }

    @DeleteMapping("delete/{mealId}")
    public void deleteMeal(@PathVariable("mealId") Long mealId){
        this.mealService.deleteMeal(mealId);
    }
}
