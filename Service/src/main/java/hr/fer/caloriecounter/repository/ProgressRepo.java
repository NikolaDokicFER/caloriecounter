package hr.fer.caloriecounter.repository;

import hr.fer.caloriecounter.model.Progress;
import hr.fer.caloriecounter.model.ProgressAndMealKey;
import org.springframework.data.jpa.repository.JpaRepository;

import java.sql.Date;
import java.util.Optional;

public interface ProgressRepo extends JpaRepository<Progress, ProgressAndMealKey> {
    Optional<Progress> getByUserIdAndDate(Long userId, Date date);

    boolean existsByUserIdAndDate(Long userId, Date date);
}
