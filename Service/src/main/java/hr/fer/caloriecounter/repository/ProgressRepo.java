package hr.fer.caloriecounter.repository;

import hr.fer.caloriecounter.model.Progress;
import hr.fer.caloriecounter.model.ProgressKey;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ProgressRepo extends JpaRepository<Progress, ProgressKey> {
    List<Progress> getAllByUserId(Long userId);

    boolean existsByUserIdAndDate(Long userId, LocalDate date);

    boolean existsByUserId(Long userId);

    void deleteByUserIdAndDate(Long userId, LocalDate date);
}
