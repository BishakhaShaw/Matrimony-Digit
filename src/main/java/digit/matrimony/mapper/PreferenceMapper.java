package digit.matrimony.mapper;

import digit.matrimony.dto.PreferenceDTO;
import digit.matrimony.entity.Preference;
import digit.matrimony.entity.User;
import org.springframework.stereotype.Component;

@Component
public class PreferenceMapper {

    // Convert Preference entity to PreferenceDTO
    public PreferenceDTO toDto(Preference preference) {
        if (preference == null) return null;

        return PreferenceDTO.builder()
                .id(preference.getId())
                .userId(preference.getUser() != null ? preference.getUser().getId() : null)
                .preferredAgeMin(preference.getPreferredAgeMin())
                .preferredAgeMax(preference.getPreferredAgeMax())
                .preferredReligion(preference.getPreferredReligion())
                .preferredCaste(preference.getPreferredCaste())
                .preferredLocation(preference.getPreferredLocation())
                .preferredEducation(preference.getPreferredEducation())
                .preferredMaritalStatus(preference.getPreferredMaritalStatus())
                .build();
    }

    // Convert PreferenceDTO to Preference entity
    public Preference toEntity(PreferenceDTO dto, User user) {
        if (dto == null) return null;

        return Preference.builder()
                .id(dto.getId())
                .user(user)
                .preferredAgeMin(dto.getPreferredAgeMin())
                .preferredAgeMax(dto.getPreferredAgeMax())
                .preferredReligion(dto.getPreferredReligion())
                .preferredCaste(dto.getPreferredCaste())
                .preferredLocation(dto.getPreferredLocation())
                .preferredEducation(dto.getPreferredEducation())
                .preferredMaritalStatus(dto.getPreferredMaritalStatus())
                .build();
    }
}

