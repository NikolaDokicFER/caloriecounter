package hr.fer.caloriecounter.model;

import com.sun.istack.NotNull;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.sql.Date;
import java.util.UUID;

@Getter
@Setter
@ToString
@RequiredArgsConstructor

@Entity
@Table(name="progress")
@IdClass(ProgressAndMealKey.class)
public class Progress {
    @Id
    private Long userId;

    @Id
    private Date date;

    @Column
    @NotNull
    private Float weight;

    @Id
    @GeneratedValue(generator = "UUID", strategy = GenerationType.IDENTITY)
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    private UUID image;

    @Column
    @NotNull
   // @Enumerated(EnumType.STRING)
    private int imageExtension;

}
