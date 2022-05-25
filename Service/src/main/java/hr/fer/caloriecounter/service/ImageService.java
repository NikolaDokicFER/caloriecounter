package hr.fer.caloriecounter.service;

import hr.fer.caloriecounter.exception.IncorrectImageException;
import hr.fer.caloriecounter.model.Image;
import hr.fer.caloriecounter.model.enums.ImageExtension;
import hr.fer.caloriecounter.repository.ImageRepo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Locale;
import java.util.Objects;
import java.util.UUID;

@Service
@AllArgsConstructor
public class ImageService {
    private final ImageRepo imageRepository;
    private final Path root = Paths.get("images");

    public Image saveImageFile(MultipartFile image) {
        Image savedImage = new Image();
        try {
            String extension = Objects.requireNonNull(image.getOriginalFilename()).substring(image.getOriginalFilename().lastIndexOf(".") + 1).toLowerCase(Locale.ROOT);

            ImageExtension imageExtension;
            switch (extension) {
                case "png":
                    imageExtension = ImageExtension.PNG;
                    break;
                case "jpeg":
                    imageExtension = ImageExtension.JPEG;
                    break;
                case "jpg":
                    imageExtension = ImageExtension.JPG;
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + extension);
            }

            savedImage.setImageExtension(imageExtension);
            savedImage = this.imageRepository.save(savedImage);

            this.createBaseDirIfNotExists();

            Files.copy(image.getInputStream(), this.root.resolve(savedImage.getFileName()));
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return savedImage;
    }

    public Image getImageByUuid(UUID imageUuid){
        return this.imageRepository.findById(imageUuid).orElseThrow(() -> new IncorrectImageException("Image not found"));
    }

    public byte[] getImageFile(Image image) {
        try {
            return Files.readAllBytes(this.root.resolve(image.getFileName()));
        } catch (IOException e) {
            throw new IncorrectImageException("Image not found");
        }
    }

    private void createBaseDirIfNotExists() throws IOException {
        File dir = this.root.toFile();
        if (!dir.exists()) {
            if (!dir.mkdir()) {
                throw new IOException();
            }
        }
    }
}