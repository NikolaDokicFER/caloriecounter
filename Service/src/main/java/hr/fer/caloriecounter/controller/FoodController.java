package hr.fer.caloriecounter.controller;

import hr.fer.caloriecounter.model.Food;
import hr.fer.caloriecounter.service.FoodService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("api/food")
@CrossOrigin("*")
public class FoodController {
    private final FoodService foodService;

    @PostMapping
    public Food saveFood(@RequestBody Food food){
        return this.foodService.saveFood(food);
    }

    @GetMapping("{name}")
    public Food getFood(@PathVariable String name){
        return this.foodService.getFood(name);
    }
}
