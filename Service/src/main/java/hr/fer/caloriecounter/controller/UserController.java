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

    @PostMapping("update")
    public User updateUser(@RequestBody User user){
        return this.userService.updateUser(user);
    }

    @GetMapping("{username}/{password}")
    public User getUser(@PathVariable String username, @PathVariable String password){
        return this.userService.getUser(username, password);
    }
}
