

package digit.matrimony.service;

import digit.matrimony.dto.ProfileDTO;
import digit.matrimony.entity.Profile;
import digit.matrimony.entity.ProfilePhoto;
import digit.matrimony.entity.User;
import digit.matrimony.exception.BadRequestException;
import digit.matrimony.exception.ResourceNotFoundException;
import digit.matrimony.mapper.ProfileMapper;
import digit.matrimony.repository.ProfileRepository;
import digit.matrimony.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProfileService {

    private final ProfileRepository profileRepository;
    private final UserRepository userRepository;

    public List<ProfileDTO> getAllProfiles() {
        return profileRepository.findAll().stream()
                .map(ProfileMapper::toDTO)
                .toList();
    }

    public ProfileDTO getProfileById(Long id) {
        Profile profile = profileRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Profile not found with id: " + id));
        return ProfileMapper.toDTO(profile);
    }

    public ProfileDTO createProfile(ProfileDTO dto) {
        // Validate userId
        if (dto.getUserId() == null) {
            throw new BadRequestException("UserId is required to create a profile.");
        }

        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + dto.getUserId()));

        // Validate photos
        if (dto.getPhotos() == null || dto.getPhotos().size() < 2) {
            throw new BadRequestException("At least 2 photos are required to create a profile.");
        }

        long profilePhotoCount = dto.getPhotos().stream()
                .filter(photo -> Boolean.TRUE.equals(photo.getIsProfilePhoto()))
                .count();
        if (profilePhotoCount != 1) {
            throw new BadRequestException("Exactly 1 photo must be marked as profile photo.");
        }

        // Map DTO -> Entity
        Profile profile = ProfileMapper.toEntity(dto);
        profile.setUser(user);

        // Link all photos to this profile
        for (ProfilePhoto photo : profile.getPhotos()) {
            photo.setProfile(profile);
        }

        // Save and return
        profile = profileRepository.save(profile);
        return ProfileMapper.toDTO(profile);
    }

    public ProfileDTO updateProfile(Long id, ProfileDTO dto) {
        Profile existing = profileRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Profile not found with id: " + id));

        // Map fields
        existing.setGender(dto.getGender());
        existing.setDateOfBirth(dto.getDateOfBirth());
        existing.setPhone(dto.getPhone());
        existing.setAadhaarNumber(dto.getAadhaarNumber());
        existing.setBio(dto.getBio());
        existing.setReligion(dto.getReligion());
        existing.setCaste(dto.getCaste());
        existing.setEducation(dto.getEducation());
        existing.setOccupation(dto.getOccupation());
        existing.setIncome(dto.getIncome());
        existing.setCurrentLocation(dto.getCurrentLocation());
        existing.setHeight(dto.getHeight());
        existing.setMaritalStatus(dto.getMaritalStatus());
        existing.setIsVerified(dto.getIsVerified());

        // Validate and update photos if provided
        if (dto.getPhotos() != null && !dto.getPhotos().isEmpty()) {
            if (dto.getPhotos().size() < 2) {
                throw new BadRequestException("At least 2 photos are required to update a profile.");
            }
            long profilePhotoCount = dto.getPhotos().stream()
                    .filter(photo -> Boolean.TRUE.equals(photo.getIsProfilePhoto()))
                    .count();
            if (profilePhotoCount != 1) {
                throw new BadRequestException("Exactly 1 photo must be marked as profile photo.");
            }

            List<ProfilePhoto> updatedPhotos = dto.getPhotos().stream()
                    .map(photoDTO -> {
                        ProfilePhoto photo = digit.matrimony.mapper.ProfilePhotoMapper.toEntity(photoDTO);
                        photo.setProfile(existing);
                        return photo;
                    }).toList();

            existing.getPhotos().clear();
            existing.getPhotos().addAll(updatedPhotos);
        }

        // Update user if provided
        if (dto.getUserId() != null) {
            User user = userRepository.findById(dto.getUserId())
                    .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + dto.getUserId()));
            existing.setUser(user);
        }

        profileRepository.save(existing);
        return ProfileMapper.toDTO(existing);
    }

    public void deleteProfile(Long id) {
        Profile profile = profileRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Profile not found with id: " + id));
        profileRepository.delete(profile);
    }
}
