package hr.fer.caloriecounter.repository;

import hr.fer.caloriecounter.model.Food;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FoodRepo extends JpaRepository<Food, Long> {
    Optional<Food> findByName(String name);

    boolean existsByName(String name);
}
