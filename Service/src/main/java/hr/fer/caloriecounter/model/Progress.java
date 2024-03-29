package hr.fer.caloriecounter.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sun.istack.NotNull;
import hr.fer.caloriecounter.model.enums.ImageExtension;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Locale;
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

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "image_id")
    @ToString.Exclude
    private Image image;
}
