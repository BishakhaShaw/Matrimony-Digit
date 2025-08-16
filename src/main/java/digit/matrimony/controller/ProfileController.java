//
//package digit.matrimony.controller;
//
//import digit.matrimony.dto.ProfileDTO;
//import digit.matrimony.service.ProfileService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@RestController
//@RequestMapping("/api/profiles")
//@RequiredArgsConstructor
//public class ProfileController {
//
//    private final ProfileService profileService;
//
//    // Get all profiles
//    @GetMapping
//    public ResponseEntity<List<ProfileDTO>> getAllProfiles() {
//        return ResponseEntity.ok(profileService.getAllProfiles());
//    }
//
//    // Get profile by ID
//    @GetMapping("/{id}")
//    public ResponseEntity<ProfileDTO> getProfile(@PathVariable Long id) {
//        return ResponseEntity.ok(profileService.getProfileById(id));
//    }
//
//    // Create new profile
//    @PostMapping
//    public ResponseEntity<ProfileDTO> createProfile(@RequestBody ProfileDTO profileDTO) {
//        return ResponseEntity.ok(profileService.createProfile(profileDTO));
//    }
//
//    // Update existing profile
//    @PutMapping("/{id}")
//    public ResponseEntity<ProfileDTO> updateProfile(@PathVariable Long id, @RequestBody ProfileDTO profileDTO) {
//        return ResponseEntity.ok(profileService.updateProfile(id, profileDTO));
//    }
//
//    // Delete profile
//    @DeleteMapping("/{id}")
//    public ResponseEntity<Void> deleteProfile(@PathVariable Long id) {
//        profileService.deleteProfile(id);
//        return ResponseEntity.noContent().build();
//    }
//}























package digit.matrimony.controller;

import digit.matrimony.dto.ProfileDTO;
import digit.matrimony.service.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/profiles")
@RequiredArgsConstructor
public class ProfileController {

    private final ProfileService profileService;

    @GetMapping
    public List<ProfileDTO> getAllProfiles() {
        return profileService.getAllProfiles();
    }

    @GetMapping("/{id}")
    public ProfileDTO getProfile(@PathVariable Long id) {
        return profileService.getProfileById(id);
    }

    @PostMapping
    public ProfileDTO createProfile(@RequestBody ProfileDTO profileDTO) {
        return profileService.createProfile(profileDTO);
    }

    @PutMapping("/{id}")
    public ProfileDTO updateProfile(@PathVariable Long id, @RequestBody ProfileDTO profileDTO) {
        return profileService.updateProfile(id, profileDTO);
    }

    @DeleteMapping("/{id}")
    public void deleteProfile(@PathVariable Long id) {
        profileService.deleteProfile(id);
    }
}
