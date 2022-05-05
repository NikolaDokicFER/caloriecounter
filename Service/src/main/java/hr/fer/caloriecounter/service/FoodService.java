package hr.fer.caloriecounter.service;

import hr.fer.caloriecounter.model.Food;
import hr.fer.caloriecounter.repository.FoodRepo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class FoodService {
    private final FoodRepo foodRepository;

    public Food saveFood(Food food){
        if(this.foodRepository.existsByName(food.getName())){
            System.out.println("Hrana s tim imenom već postoji");
            return null;
        }else{
            return this.foodRepository.save(food);
        }
    }

    public Food getFood(String name) throws Exception{
        return this.foodRepository.findByName(name).orElseThrow(Exception::new);
    }
}
