package digit.matrimony.service;

import digit.matrimony.dto.PreferenceDTO;
import digit.matrimony.entity.Preference;
import digit.matrimony.entity.User;
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

    // ✅ Get preference by user ID
    public PreferenceDTO getPreferenceByUserId(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));

        Preference preference = preferenceRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Preference not found for user ID: " + userId));

        return preferenceMapper.toDto(preference);
    }

    // ✅ Create new preference
    public PreferenceDTO createPreference(PreferenceDTO dto) {
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + dto.getUserId()));

        if (preferenceRepository.findByUser(user).isPresent()) {
            throw new RuntimeException("Preference already exists for user ID: " + dto.getUserId());
        }

        Preference preference = preferenceMapper.toEntity(dto, user);
        Preference saved = preferenceRepository.save(preference);
        return preferenceMapper.toDto(saved);
    }

    // ✅ Update existing preference
    public PreferenceDTO updatePreference(Long id, PreferenceDTO dto) {
        Preference existing = preferenceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Preference not found with ID: " + id));

        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + dto.getUserId()));

        // Update fields
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

    // ✅ Delete preference
    public void deletePreference(Long id) {
        preferenceRepository.deleteById(id);
    }
}
