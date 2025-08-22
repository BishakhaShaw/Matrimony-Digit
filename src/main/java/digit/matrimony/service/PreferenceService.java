package digit.matrimony.service;

import digit.matrimony.dto.PreferenceDTO;
import digit.matrimony.entity.Preference;
import digit.matrimony.entity.User;
import digit.matrimony.exception.BadRequestException;
import digit.matrimony.exception.ResourceNotFoundException;
import digit.matrimony.mapper.PreferenceMapper;
import digit.matrimony.repository.PreferenceRepository;
import digit.matrimony.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PreferenceService {

    private final PreferenceRepository preferenceRepository;
    private final UserRepository userRepository;
    private final PreferenceMapper preferenceMapper;

    public PreferenceDTO getPreferenceByUserId(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + userId));

        Preference preference = preferenceRepository.findByUser(user)
                .orElseThrow(() -> new ResourceNotFoundException("Preference not found for user ID: " + userId));

        return preferenceMapper.toDto(preference);
    }

    public PreferenceDTO createPreference(PreferenceDTO dto) {
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + dto.getUserId()));

        if (preferenceRepository.findByUser(user).isPresent()) {
            throw new BadRequestException("Preference already exists for user ID: " + dto.getUserId());
        }

        Preference preference = preferenceMapper.toEntity(dto, user);
        Preference saved = preferenceRepository.save(preference);
        return preferenceMapper.toDto(saved);
    }

    public PreferenceDTO updatePreference(Long id, PreferenceDTO dto) {
        Preference existing = preferenceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Preference not found with ID: " + id));

        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + dto.getUserId()));

        existing.setUser(user);
        existing.setPreferredAgeMin(dto.getPreferredAgeMin());
        existing.setPreferredAgeMax(dto.getPreferredAgeMax());
        existing.setPreferredReligion(dto.getPreferredReligion());
        existing.setPreferredCaste(dto.getPreferredCaste());
        existing.setPreferredLocation(dto.getPreferredLocation());
        existing.setPreferredEducation(dto.getPreferredEducation());
        existing.setPreferredMaritalStatus(dto.getPreferredMaritalStatus());
        existing.setPreferredGender(dto.getPreferredGender());

        Preference updated = preferenceRepository.save(existing);
        return preferenceMapper.toDto(updated);
    }

    public void deletePreference(Long id) {
        preferenceRepository.deleteById(id);
    }
}
