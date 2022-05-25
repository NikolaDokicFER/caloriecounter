package hr.fer.caloriecounter.controller;

import hr.fer.caloriecounter.model.Progress;
import hr.fer.caloriecounter.service.ProgressService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping("{userId}")
    public List<Progress> getProgress(@PathVariable Long userId){
        return this.progressService.getProgress(userId);
    }
}
