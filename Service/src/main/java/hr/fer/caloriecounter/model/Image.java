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
import java.util.Locale;
import java.util.UUID;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
public class Image {
    @Id
    @GeneratedValue(generator = "UUID", strategy = GenerationType.IDENTITY)
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    private UUID uuid;

    @Column
    @NotNull
    @Enumerated(EnumType.STRING)
    private ImageExtension imageExtension;

    @JsonIgnore
    @Transient
    public String getFileName(){
        return this.uuid.toString() + "." + this.imageExtension.name().toLowerCase(Locale.ROOT);
    }
}
