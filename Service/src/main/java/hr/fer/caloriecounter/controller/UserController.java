package hr.fer.caloriecounter.controller;

import hr.fer.caloriecounter.model.User;
import hr.fer.caloriecounter.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.security.SecureRandom;

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

    @GetMapping("{username}/{password}")
    public User getUser(@PathVariable String username, @PathVariable String password){
        return this.userService.getUser(username, password);
    }
}
