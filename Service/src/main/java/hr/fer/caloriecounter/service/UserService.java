package hr.fer.caloriecounter.service;

import hr.fer.caloriecounter.model.User;
import hr.fer.caloriecounter.repository.UserRepo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

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
            return this.userRepository.save(user);
        }
    }

    public User getUser(String username) throws Exception{
        return this.userRepository.findByUsername(username).orElseThrow(Exception::new);
    }
}
