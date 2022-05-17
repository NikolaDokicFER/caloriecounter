package hr.fer.caloriecounter.model;

import hr.fer.caloriecounter.model.enums.MealType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Meal {
    private long id;
    private String date;
    private MealType type;
    private Float quantity;
    private Food food;
    private UserDetail user;
}
