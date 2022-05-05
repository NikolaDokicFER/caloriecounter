package hr.fer.caloriecounter.service;

import hr.fer.caloriecounter.model.Progress;
import hr.fer.caloriecounter.repository.ProgressRepo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Date;

@Service
@AllArgsConstructor
public class ProgressService {
    private final ProgressRepo progressRepository;

    public Progress saveProgress(Progress progress){
        if(this.progressRepository.existsByUserIdAndDate(progress.getUserId(), progress.getDate())){
            System.out.println("Postoji vec");
            return null;
        }else{
            return this.progressRepository.save(progress);
        }
    }

    public Progress getProgress(Long userId, Date date) throws Exception{
        return this.progressRepository.getByUserIdAndDate(userId, date).orElseThrow(Exception::new);
    }
}
