
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
