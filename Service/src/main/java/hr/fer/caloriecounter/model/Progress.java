package hr.fer.caloriecounter.model;

import com.sun.istack.NotNull;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@ToString
@RequiredArgsConstructor

@Entity
@Table(name="progress")
@IdClass(ProgressKey.class)
public class Progress {
    @Id
    private Long userId;

    @Id
    private LocalDate date;

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
