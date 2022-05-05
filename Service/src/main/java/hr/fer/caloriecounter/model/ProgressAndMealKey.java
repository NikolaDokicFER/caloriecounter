package hr.fer.caloriecounter.model;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.sql.Date;

@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class ProgressAndMealKey implements Serializable {
    private Long userId;
    private Date date;

    ProgressAndMealKey(){
    }
}
