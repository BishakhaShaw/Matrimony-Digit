//package digit.matrimony.controller;
//
//import digit.matrimony.dto.ProfilePhotoDTO;
//import digit.matrimony.service.ProfilePhotoService;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
///**
// * REST Controller for managing ProfilePhoto entities.
// * Provides endpoints for retrieving all photos, a single photo by ID, and creating a new photo.
// */
//@RestController
//@RequestMapping("/api/profile-photos")
//public class ProfilePhotoController {
//
//    private final ProfilePhotoService profilePhotoService;
//
//    public ProfilePhotoController(ProfilePhotoService profilePhotoService) {
//        this.profilePhotoService = profilePhotoService;
//    }
//
//    /**
//     * Retrieves all profile photos.
//     *
//     * @return A list of all ProfilePhotoDTOs.
//     */
//    @GetMapping
//    public List<ProfilePhotoDTO> getAllProfilePhotos() {
//        return profilePhotoService.getAllProfilePhotos();
//    }
//
//    /**
//     * Retrieves a profile photo by its ID.
//     *
//     * @param id The ID of the photo to retrieve.
//     * @return The ProfilePhotoDTO for the specified ID.
//     */
//    @GetMapping("/{id}")
//    public ProfilePhotoDTO getProfilePhoto(@PathVariable Long id) {
//        return profilePhotoService.getProfilePhotoById(id);
//    }
//
//    /**
//     * Creates a new profile photo.
//     *
//     * @param profilePhotoDTO The ProfilePhotoDTO to create.
//     * @return The created ProfilePhotoDTO.
//     */
//    @PostMapping
//    public ProfilePhotoDTO createProfilePhoto(@RequestBody ProfilePhotoDTO profilePhotoDTO) {
//        return profilePhotoService.createProfilePhoto(profilePhotoDTO);
//    }
//}









package digit.matrimony.controller;

import digit.matrimony.dto.ProfilePhotoDTO;
import digit.matrimony.service.ProfilePhotoService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/profile-photos")
@RequiredArgsConstructor
public class ProfilePhotoController {

    private final ProfilePhotoService profilePhotoService;

    @GetMapping
    public List<ProfilePhotoDTO> getAllProfilePhotos() {
        return profilePhotoService.getAllProfilePhotos();
    }

    @GetMapping("/{id}")
    public ProfilePhotoDTO getProfilePhoto(@PathVariable Long id) {
        return profilePhotoService.getProfilePhotoById(id);
    }

    @PostMapping
    public ProfilePhotoDTO createProfilePhoto(@RequestBody ProfilePhotoDTO profilePhotoDTO) {
        return profilePhotoService.createProfilePhoto(profilePhotoDTO);
    }

    @PutMapping("/{id}")
    public ProfilePhotoDTO updateProfilePhoto(@PathVariable Long id, @RequestBody ProfilePhotoDTO profilePhotoDTO) {
        return profilePhotoService.updateProfilePhoto(id, profilePhotoDTO);
    }

    @DeleteMapping("/{id}")
    public void deleteProfilePhoto(@PathVariable Long id) {
        profilePhotoService.deleteProfilePhoto(id);
    }
}
