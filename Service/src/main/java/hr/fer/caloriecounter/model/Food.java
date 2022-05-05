package hr.fer.caloriecounter.model;

import com.sun.istack.NotNull;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Getter
@Setter
@ToString
@RequiredArgsConstructor

@Entity
@Table(name="food")
public class Food {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    @NotNull
    private String name;

    @Column
    @NotNull
    private int calories;

    @Column
    private Float fat;

    @Column
    private Float proteins;

    @Column
    private Float carbohydrates;

    @Column(name = "vitamin_a")
    private Float vitaminA;

    @Column(name = "vitamin_c")
    private Float vitaminC;

    @Column
    private Float iodine;

    @Column
    private Float calcium;

    @Column
    private Float salt;

    @Column
    private Float iron;
}
