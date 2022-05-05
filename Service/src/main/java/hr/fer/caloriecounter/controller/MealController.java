package hr.fer.caloriecounter.controller;

import hr.fer.caloriecounter.model.Meal;
import hr.fer.caloriecounter.model.Progress;
import hr.fer.caloriecounter.service.MealService;
import hr.fer.caloriecounter.service.ProgressService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;

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
    public Meal getMeal(@PathVariable Long userId, @PathVariable Date date) throws Exception {
        return this.mealService.getMeal(userId, date);
    }
}
