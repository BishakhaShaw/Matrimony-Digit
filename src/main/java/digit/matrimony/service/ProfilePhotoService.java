//package digit.matrimony.service;
//
//import digit.matrimony.dto.ProfilePhotoDTO;
//import digit.matrimony.entity.ProfilePhoto;
//import digit.matrimony.exception.ResourceNotFoundException;
//import digit.matrimony.mapper.ProfilePhotoMapper;
//import digit.matrimony.repository.ProfilePhotoRepository;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//import java.util.stream.Collectors;
//
///**
// * Service class for managing ProfilePhoto entities.
// */
//@Service
//@RequiredArgsConstructor
//public class ProfilePhotoService {
//
//    private final ProfilePhotoRepository profilePhotoRepository;
//
//    /**
//     * Retrieves a list of all profile photos.
//     *
//     * @return A list of ProfilePhotoDTOs.
//     */
//    public List<ProfilePhotoDTO> getAllProfilePhotos() {
//        return profilePhotoRepository.findAll().stream()
//                .map(ProfilePhotoMapper::toDTO) // static mapper
//                .collect(Collectors.toList());
//    }
//
//    /**
//     * Retrieves a single profile photo by its ID.
//     *
//     * @param id The ID of the photo to retrieve.
//     * @return The ProfilePhotoDTO for the specified ID.
//     * @throws ResourceNotFoundException if no photo is found with the given ID.
//     */
//    public ProfilePhotoDTO getProfilePhotoById(Long id) {
//        ProfilePhoto profilePhoto = profilePhotoRepository.findById(id)
//                .orElseThrow(() -> new ResourceNotFoundException("ProfilePhoto not found"));
//        return ProfilePhotoMapper.toDTO(profilePhoto);
//    }
//
//    /**
//     * Creates a new profile photo from a DTO.
//     *
//     * @param dto The ProfilePhotoDTO to create.
//     * @return The created ProfilePhotoDTO.
//     */
//    public ProfilePhotoDTO createProfilePhoto(ProfilePhotoDTO dto) {
//        ProfilePhoto profilePhoto = ProfilePhotoMapper.toEntity(dto);
//        profilePhoto = profilePhotoRepository.save(profilePhoto);
//        return ProfilePhotoMapper.toDTO(profilePhoto);
//    }
//
//    /**
//     * Deletes a profile photo by its ID.
//     *
//     * @param id The ID of the photo to delete.
//     * @throws ResourceNotFoundException if no photo is found with the given ID.
//     */
//    public void deleteProfilePhoto(Long id) {
//        ProfilePhoto profilePhoto = profilePhotoRepository.findById(id)
//                .orElseThrow(() -> new ResourceNotFoundException("ProfilePhoto not found"));
//        profilePhotoRepository.delete(profilePhoto);
//    }
//}







package digit.matrimony.service;

import digit.matrimony.dto.ProfilePhotoDTO;
import digit.matrimony.entity.Profile;
import digit.matrimony.entity.ProfilePhoto;
import digit.matrimony.exception.BadRequestException;
import digit.matrimony.exception.ResourceNotFoundException;
import digit.matrimony.mapper.ProfilePhotoMapper;
import digit.matrimony.repository.ProfilePhotoRepository;
import digit.matrimony.repository.ProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProfilePhotoService {

    private final ProfilePhotoRepository profilePhotoRepository;
    private final ProfileRepository profileRepository;

    public List<ProfilePhotoDTO> getAllProfilePhotos() {
        return profilePhotoRepository.findAll().stream()
                .map(ProfilePhotoMapper::toDTO)
                .collect(Collectors.toList());
    }

    public ProfilePhotoDTO getProfilePhotoById(Long id) {
        ProfilePhoto photo = profilePhotoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ProfilePhoto not found with id: " + id));
        return ProfilePhotoMapper.toDTO(photo);
    }

    public ProfilePhotoDTO createProfilePhoto(ProfilePhotoDTO dto) {
        if (dto.getProfileId() == null) {
            throw new BadRequestException("ProfileId is required for a photo.");
        }
        Profile profile = profileRepository.findById(dto.getProfileId())
                .orElseThrow(() -> new ResourceNotFoundException("Profile not found"));

        ProfilePhoto photo = ProfilePhotoMapper.toEntity(dto);
        photo.setProfile(profile);
        photo = profilePhotoRepository.save(photo);
        return ProfilePhotoMapper.toDTO(photo);
    }

    public ProfilePhotoDTO updateProfilePhoto(Long id, ProfilePhotoDTO dto) {
        ProfilePhoto existing = profilePhotoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ProfilePhoto not found with id: " + id));

        existing.setPhotoUrl(dto.getPhotoUrl());
        existing.setIsProfilePhoto(dto.getIsProfilePhoto());

        if (dto.getProfileId() != null) {
            Profile profile = profileRepository.findById(dto.getProfileId())
                    .orElseThrow(() -> new ResourceNotFoundException("Profile not found"));
            existing.setProfile(profile);
        }

        existing = profilePhotoRepository.save(existing);
        return ProfilePhotoMapper.toDTO(existing);
    }

    public void deleteProfilePhoto(Long id) {
        ProfilePhoto existing = profilePhotoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ProfilePhoto not found with id: " + id));
        profilePhotoRepository.delete(existing);
    }
}
