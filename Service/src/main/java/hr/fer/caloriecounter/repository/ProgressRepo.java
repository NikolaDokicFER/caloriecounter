package hr.fer.caloriecounter.repository;

import hr.fer.caloriecounter.model.Progress;
import hr.fer.caloriecounter.model.ProgressKey;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface ProgressRepo extends JpaRepository<Progress, ProgressKey> {
    Optional<Progress> getByUserIdAndDate(Long userId, LocalDate date);

    boolean existsByUserIdAndDate(Long userId, LocalDate date);
}
