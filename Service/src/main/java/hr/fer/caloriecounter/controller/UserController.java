package hr.fer.caloriecounter.controller;

import hr.fer.caloriecounter.model.User;
import hr.fer.caloriecounter.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("api/user")
@CrossOrigin("*")
public class UserController {
    private final UserService userService;

    @PostMapping
    public User saveUser(@RequestBody User user){
        return this.userService.saveUser(user);
    }

    @GetMapping("{username}")
    public User getUser(@PathVariable String username) throws Exception {
        return this.userService.getUser(username);
    }
}
