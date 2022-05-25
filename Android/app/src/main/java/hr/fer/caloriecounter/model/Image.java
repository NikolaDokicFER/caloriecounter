package hr.fer.caloriecounter.model;

import java.util.UUID;

import hr.fer.caloriecounter.model.enums.ImageExtension;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Image {
    private UUID uuid;
    private ImageExtension imageExtension;
}
