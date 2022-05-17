package hr.fer.caloriecounter.model;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;

@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class ProgressKey implements Serializable {
    private Long userId;
    private LocalDate date;

    ProgressKey(){
    }
}
