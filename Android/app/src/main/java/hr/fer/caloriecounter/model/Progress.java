package hr.fer.caloriecounter.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Progress implements Comparable<Progress> {
    private Long userId;
    private String date;
    private Float weight;
    private Image image;

    @Override
    public int compareTo(Progress other) {
        String[] date1 = this.date.split("-");
        String[] date2 = other.date.split("-");

        if (date1[0].compareTo(date2[0]) == 0) {
            if (date1[1].compareTo(date2[1]) == 0) {
                return date1[2].compareTo(date2[2]);
            } else {
                return date1[1].compareTo(date2[1]);
            }
        } else {
            return date1[0].compareTo(date2[0]);
        }
    }
}
