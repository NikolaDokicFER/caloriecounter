package hr.fer.caloriecounter.repository;

import hr.fer.caloriecounter.model.Meal;
import hr.fer.caloriecounter.model.User;
import hr.fer.caloriecounter.model.enums.MealType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface MealRepo extends JpaRepository<Meal, Long> {
    Optional<List<Meal>> getByUserAndDate(User user, LocalDate date);

    boolean existsByUserIdAndFoodIdAndDateAndType(Long userId, Long foodId,LocalDate date, MealType type);

    Meal getByUserIdAndFoodIdAndDateAndType(Long userId, Long foodId,LocalDate date, MealType type);
}
