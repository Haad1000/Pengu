package code.ProfileImages;

import code.Biography.Biography;
import code.Biography.BiographyRepository;
import code.Users.TokenStore;
import code.Users.User;
import code.Users.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.Column;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.UUID;

@RestController
public class ImageController {

    @Value("${image.upload.dir}")
    private String directoryPath;

    @Autowired
    private ImageRepository imageRepository;

    @Autowired
    private TokenStore tokenStore;

    @Autowired
    private BiographyRepository biographyRepository;


    @GetMapping(value = "/images/user/{userId}", produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<byte[]> getImageByUserId(@PathVariable int userId) {
        try {
            Biography bio = biographyRepository.findByUserId(userId);
            if (bio == null || bio.getImage() == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }

            Image image = bio.getImage();
            File imageFile = new File(image.getFilePath());
            byte[] imageData = Files.readAllBytes(imageFile.toPath());
            return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(imageData);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }



    @PostMapping("/images/{token}")
    public ResponseEntity<String> uploadImage(
            @RequestParam("image") MultipartFile imageFile,
            @PathVariable String token) {
        if (imageFile.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No image file provided.");
        }

        try {
            User currentUser = tokenStore.getUser(token);
            if (currentUser == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token");
            }

            String uniqueFilename = UUID.randomUUID().toString() + "_" + imageFile.getOriginalFilename();
            File destinationFile = new File(directoryPath, uniqueFilename);
            imageFile.transferTo(destinationFile);

            Image image = new Image(destinationFile.getAbsolutePath(), currentUser);
            imageRepository.save(image);

            Biography bio = biographyRepository.findByUserId(currentUser.getId());
            if (bio == null) {
                bio = new Biography();
                bio.setUser(currentUser);
            }
            bio.setImage(image);
            biographyRepository.save(bio);

            return ResponseEntity.ok("File uploaded successfully: " + destinationFile.getAbsolutePath());
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to upload file: " + e.getMessage());
        }
    }


    @PutMapping("/images/update/{token}")
    public ResponseEntity<String> updateImage(
            @RequestParam("image") MultipartFile newImageFile,
            @PathVariable String token) {
        if (newImageFile.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No image file provided.");
        }

        try {
            User currentUser = tokenStore.getUser(token);
            if (currentUser == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token");
            }

            Biography bio = biographyRepository.findByUserId(currentUser.getId());
            Image currentImage = (bio != null) ? bio.getImage() : null;

            if (currentImage != null && currentImage.getFilePath() != null) {
                File oldImageFile = new File(currentImage.getFilePath());
                if (oldImageFile.exists() && !oldImageFile.delete()) {
                    throw new IOException("Failed to delete old image file");
                }
            } else {
                currentImage = new Image();
                if (bio == null) {
                    bio = new Biography();
                    bio.setUser(currentUser);
                }
                bio.setImage(currentImage);
            }

            String uniqueFilename = UUID.randomUUID().toString() + "_" + newImageFile.getOriginalFilename();
            File destinationFile = new File(directoryPath, uniqueFilename);
            newImageFile.transferTo(destinationFile);

            currentImage.setFilePath(destinationFile.getAbsolutePath());
            imageRepository.save(currentImage);
            biographyRepository.save(bio);

            return ResponseEntity.ok("Image updated successfully: " + destinationFile.getAbsolutePath());
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error during file update: " + e.getMessage());
        }
    }




    @DeleteMapping("/images/deleteImage/{token}")
    public ResponseEntity<String> deleteImage(@PathVariable String token) {
        try {
            // Validate and get the user from the token
            User currentUser = tokenStore.getUser(token);
            if (currentUser == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token");
            }

            // Get the biography for the current user to find the associated image
            Biography bio = biographyRepository.findByUserId(currentUser.getId());
            if (bio == null || bio.getImage() == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No image found to delete");
            }

            // Get the image and delete the file from the system
            Image image = bio.getImage();
            String filePath = image.getFilePath();
            if (filePath != null) {
                File file = new File(filePath);
                if (file.exists() && !file.delete()) {
                    throw new IOException("Failed to delete image file");
                }
            }

            // Remove the image association from the biography and update
            bio.setImage(null);
            biographyRepository.save(bio);

            // Delete the image from the repository
            imageRepository.delete(image);

            return ResponseEntity.ok("Image deleted successfully");

        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error deleting image: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: " + e.getMessage());
        }
    }

    @DeleteMapping("/deleteAllImages")
    public String deleteAllImages(){
        if(!imageRepository.findAll().isEmpty()){
            imageRepository.deleteAll();
            return "success";
        }
        return "failure";
    }


}