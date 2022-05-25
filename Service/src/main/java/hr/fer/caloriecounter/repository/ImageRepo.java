package hr.fer.caloriecounter.repository;

import hr.fer.caloriecounter.model.Image;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ImageRepo extends JpaRepository<Image, UUID> {
}
