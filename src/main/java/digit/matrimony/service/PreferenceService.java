package digit.matrimony.service;

import digit.matrimony.dto.PreferenceDTO;
import digit.matrimony.entity.Preference;
import digit.matrimony.entity.User;
import digit.matrimony.mapper.PreferenceMapper;
import digit.matrimony.repository.PreferenceRepository;
import digit.matrimony.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PreferenceService {

    private final PreferenceRepository preferenceRepository;
    private final UserRepository userRepository;
    private final PreferenceMapper preferenceMapper;

    public PreferenceDTO getPreferenceByUserId(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));

        Preference preference = preferenceRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Preference not found for user ID: " + userId));

        return preferenceMapper.toDto(preference);
    }

    public PreferenceDTO saveOrUpdatePreference(PreferenceDTO dto) {
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + dto.getUserId()));

        Optional<Preference> existingPreference = preferenceRepository.findByUser(user);

        Preference preference = preferenceMapper.toEntity(dto, user);

        if (existingPreference.isPresent()) {
            preference.setId(existingPreference.get().getId()); // update existing
        }

        Preference saved = preferenceRepository.save(preference);
        return preferenceMapper.toDto(saved);
    }

    public void deletePreference(Long id) {
        preferenceRepository.deleteById(id);
    }
}
