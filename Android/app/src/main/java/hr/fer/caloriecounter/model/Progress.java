package hr.fer.caloriecounter.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Progress {
    private Long userId;
    private String date;
    private Float weight;
    private Image image;
}
