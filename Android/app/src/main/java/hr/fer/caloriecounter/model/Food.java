package hr.fer.caloriecounter.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Food {
    private Long id;
    private String name;
    private int calories;
    private Float fat;
    private Float proteins;
    private Float carbohydrates;
    private Float vitaminA;
    private Float vitaminC;
    private Float iodine;
    private Float calcium;
    private Float salt;
    private Float iron;
}
