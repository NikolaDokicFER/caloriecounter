package hr.fer.caloriecounter.service;

import hr.fer.caloriecounter.exception.FoodExistsException;
import hr.fer.caloriecounter.exception.FoodNotFound;
import hr.fer.caloriecounter.model.Food;
import hr.fer.caloriecounter.repository.FoodRepo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class FoodService {
    private final FoodRepo foodRepository;

    public Food saveFood(Food food){
        if(this.foodRepository.existsByName(food.getName())){
            throw new FoodExistsException("Food already exists");
        }else{
            return this.foodRepository.save(food);
        }
    }

    public List<Food> getAllFood(){
        return this.foodRepository.findAll();
    }

    public Food getFood(String name){
        return this.foodRepository.findByName(name).orElseThrow(() ->
            new FoodNotFound("Food not found"));
    }
}
