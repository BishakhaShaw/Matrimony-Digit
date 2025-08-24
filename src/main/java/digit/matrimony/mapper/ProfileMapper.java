

package digit.matrimony.mapper;

import digit.matrimony.dto.ProfileDTO;
import digit.matrimony.entity.Profile;
import digit.matrimony.entity.ProfilePhoto;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ProfileMapper {

    private ProfileMapper() {
        // Prevent instantiation
    }

    public static ProfileDTO toDTO(Profile profile) {
        if (profile == null) return null;

        List<digit.matrimony.dto.ProfilePhotoDTO> photosDTO = new ArrayList<>();
        if (profile.getPhotos() != null) {
            for (ProfilePhoto photo : profile.getPhotos()) {
                photosDTO.add(digit.matrimony.mapper.ProfilePhotoMapper.toDTO(photo));
            }
        }

        return ProfileDTO.builder()
                .id(profile.getId())
                .userId(profile.getUser() != null ? profile.getUser().getId() : null)
                .gender(profile.getGender())
                .dateOfBirth(profile.getDateOfBirth())
                .phone(profile.getPhone())
                .aadhaarNumber(profile.getAadhaarNumber())
                .bio(profile.getBio())
                .religion(profile.getReligion())
                .caste(profile.getCaste())
                .education(profile.getEducation())
                .occupation(profile.getOccupation())
                .income(profile.getIncome())
                .currentLocation(profile.getCurrentLocation())
                .height(profile.getHeight())
                .maritalStatus(profile.getMaritalStatus())
                .isVerified(profile.getIsVerified())
                .photos(photosDTO)
                .build();
    }

    public static Profile toEntity(ProfileDTO dto) {
        if (dto == null) return null;

        Profile profile = new Profile();
        profile.setId(dto.getId());
        profile.setGender(dto.getGender());
        profile.setDateOfBirth(dto.getDateOfBirth());
        profile.setPhone(dto.getPhone());
        profile.setAadhaarNumber(dto.getAadhaarNumber());
        profile.setBio(dto.getBio());
        profile.setReligion(dto.getReligion());
        profile.setCaste(dto.getCaste());
        profile.setEducation(dto.getEducation());
        profile.setOccupation(dto.getOccupation());
        profile.setIncome(dto.getIncome());
        profile.setCurrentLocation(dto.getCurrentLocation());
        profile.setHeight(dto.getHeight());
        profile.setMaritalStatus(dto.getMaritalStatus());
        profile.setIsVerified(dto.getIsVerified());

        // Map photos
        List<ProfilePhoto> photos = new ArrayList<>();
        if (dto.getPhotos() != null) {
            for (digit.matrimony.dto.ProfilePhotoDTO photoDTO : dto.getPhotos()) {
                ProfilePhoto photo = digit.matrimony.mapper.ProfilePhotoMapper.toEntity(photoDTO);
                photo.setProfile(profile); // ensure the link
                photos.add(photo);
            }
        }
        profile.setPhotos(photos);

        return profile;
    }
}
