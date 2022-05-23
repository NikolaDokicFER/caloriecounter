package hr.fer.caloriecounter.service;

import hr.fer.caloriecounter.exception.EmailExistsException;
import hr.fer.caloriecounter.exception.IncorrectPassswordException;
import hr.fer.caloriecounter.exception.UsernameExistsException;
import hr.fer.caloriecounter.model.User;
import hr.fer.caloriecounter.repository.UserRepo;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;

@Service
@AllArgsConstructor
public class UserService {
    private final UserRepo userRepository;

    public User saveUser(User user){
        if(this.userRepository.existsByEmail(user.getEmail())) {
            throw new EmailExistsException("Email already taken");
        }else if(this.userRepository.existsByUsername(user.getUsername())){
            throw new UsernameExistsException("Username already taken");
        }else{
            PasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder(10, new SecureRandom());
            user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
            return this.userRepository.save(user);
        }
    }

    public User updateUser(User user){
        User user1 = this.userRepository.getById(user.getId());
        user1.setCaloriesNeeded(user.getCaloriesNeeded());
        user1.setWeight(user.getWeight());
        return this.userRepository.save(user1);
    }

    public User getUser(String username, String password){
        User user = this.userRepository.findByUsername(username).orElseThrow(() ->
                new IncorrectPassswordException("Incorrect username or password"));
        PasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder(10, new SecureRandom());
        if(bCryptPasswordEncoder.matches(password, user.getPassword())){
            return user;
        }else{
            throw new IncorrectPassswordException("Incorrect username or password");
        }
    }
}
