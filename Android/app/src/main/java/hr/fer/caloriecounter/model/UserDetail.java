package hr.fer.caloriecounter.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDetail {
    private String firstName;
    private String lastName;
    private String email;
    private String username;
    private String password;
    private long weight;
    private long caloriesNeeded;
}
