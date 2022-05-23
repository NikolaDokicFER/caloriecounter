package hr.fer.caloriecounter.service;

import hr.fer.caloriecounter.exception.IncorrectProgressException;
import hr.fer.caloriecounter.exception.ProgressExistsException;
import hr.fer.caloriecounter.model.Progress;
import hr.fer.caloriecounter.repository.ProgressRepo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;

@Service
@AllArgsConstructor
public class ProgressService {
    private final ProgressRepo progressRepository;
    private final Path root = Paths.get("images");

    public Progress saveProgress(Progress progress, MultipartFile image){
        if(this.progressRepository.existsByUserIdAndDate(progress.getUserId(), progress.getDate())){
           throw new ProgressExistsException("Progress already exists");
        }else{
            return this.progressRepository.save(progress);
        }
    }

    public Progress getProgress(Long userId, LocalDate date){
        return this.progressRepository.getByUserIdAndDate(userId, date).orElseThrow(() ->
                new IncorrectProgressException("Progress not found"));
    }
}
