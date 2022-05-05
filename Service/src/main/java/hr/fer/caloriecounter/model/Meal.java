package hr.fer.caloriecounter.model;

import com.sun.istack.NotNull;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.sql.Date;

@Getter
@Setter
@ToString
@RequiredArgsConstructor

@Entity
@Table(name="meal")
@IdClass(ProgressAndMealKey.class)
public class Meal {
    @Id
    private Long userId;

    @Id
    private Date date;

    @Column
    @NotNull
    private int type;

    @Column
    @NotNull
    private Float quantity;

    @Column
    @NotNull
    private Long foodId;
}
