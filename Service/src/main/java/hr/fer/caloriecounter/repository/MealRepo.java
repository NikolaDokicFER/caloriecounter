package hr.fer.caloriecounter.repository;

import hr.fer.caloriecounter.model.Meal;
import hr.fer.caloriecounter.model.ProgressAndMealKey;
import org.springframework.data.jpa.repository.JpaRepository;

import java.sql.Date;
import java.util.Optional;

public interface MealRepo extends JpaRepository<Meal, ProgressAndMealKey> {
    Optional<Meal> getByUserIdAndDate(Long userId, Date date);

    boolean existsByUserIdAndDate(Long userId, Date date);
}
