package hr.fer.caloriecounter.controller;

import hr.fer.caloriecounter.model.Progress;
import hr.fer.caloriecounter.service.ProgressService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;

@RestController
@AllArgsConstructor
@RequestMapping("api/progress")
@CrossOrigin("*")
public class ProgressController {
    private final ProgressService progressService;

    @PostMapping
    public Progress saveProgress(@RequestBody Progress progress){
        return this.progressService.saveProgress(progress);
    }

    @GetMapping("{userId}/{date}")
    public Progress getProgress(@PathVariable Long userId, @PathVariable Date date) throws Exception {
        return this.progressService.getProgress(userId, date);
    }
}
