

package digit.matrimony.mapper;

import digit.matrimony.dto.ProfilePhotoDTO;
import digit.matrimony.entity.Profile;
import digit.matrimony.entity.ProfilePhoto;

public class ProfilePhotoMapper {

    private ProfilePhotoMapper() {
        // Prevent instantiation
    }

    public static ProfilePhotoDTO toDTO(ProfilePhoto photo) {
        if (photo == null) return null;

        ProfilePhotoDTO dto = new ProfilePhotoDTO();
        dto.setId(photo.getId());
        dto.setProfileId(photo.getProfile() != null ? photo.getProfile().getId() : null);
        dto.setPhotoUrl(photo.getPhotoUrl());
        dto.setIsProfilePhoto(photo.getIsProfilePhoto());
        dto.setUploadedAt(photo.getUploadedAt());
        return dto;
    }

    public static ProfilePhoto toEntity(ProfilePhotoDTO dto) {
        if (dto == null) return null;

        ProfilePhoto photo = new ProfilePhoto();
        photo.setId(dto.getId());
        photo.setPhotoUrl(dto.getPhotoUrl());
        photo.setIsProfilePhoto(dto.getIsProfilePhoto());
        photo.setUploadedAt(dto.getUploadedAt());

        // profile will be set in ProfileMapper to avoid lambda issues
        return photo;
    }
}
