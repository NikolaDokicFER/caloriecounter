package hr.fer.caloriecounter.service;

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
            System.out.println("Email vec postoji"); ///POSTAVI THROWABLE
            return null;
        }else if(this.userRepository.existsByUsername(user.getUsername())){
            System.out.println("Korisnicko ime vec postjoi"); ///POSTAVI THROWABLE
            return null;
        }else{
            PasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder(10, new SecureRandom());
            user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
            return this.userRepository.save(user);
        }
    }

    public User getUser(String username, String password) throws Exception{
        User user = this.userRepository.findByUsername(username).orElseThrow(Exception::new);
        PasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder(10, new SecureRandom());
        if(bCryptPasswordEncoder.matches(password, user.getPassword())){
            return user;
        }else{
            System.out.println("Kriva lozinka");
            return null;
        }
    }
}
