package digit.matrimony.service;

import digit.matrimony.dto.PreferenceDTO;
import digit.matrimony.entity.Preference;
import digit.matrimony.entity.User;
import digit.matrimony.exception.BadRequestException;
import digit.matrimony.exception.ResourceNotFoundException;
import digit.matrimony.mapper.PreferenceMapper;
import digit.matrimony.repository.PreferenceRepository;
import digit.matrimony.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PreferenceServiceTest {

    @Mock
    private PreferenceRepository preferenceRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PreferenceMapper preferenceMapper;

    @InjectMocks
    private PreferenceService preferenceService;

    private User user;
    private Preference preference;
    private PreferenceDTO preferenceDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        user = new User();
        user.setId(1L);

        preference = new Preference();
        preference.setId(1L);
        preference.setUser(user);

        preferenceDTO = new PreferenceDTO();
        preferenceDTO.setId(1L);
        preferenceDTO.setUserId(1L);
    }

    @Test
    void testGetPreferenceByUserId_Success() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(preferenceRepository.findByUser(user)).thenReturn(Optional.of(preference));
        when(preferenceMapper.toDto(preference)).thenReturn(preferenceDTO);

        PreferenceDTO result = preferenceService.getPreferenceByUserId(1L);
        assertEquals(1L, result.getId());
    }

    @Test
    void testGetPreferenceByUserId_UserNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> preferenceService.getPreferenceByUserId(1L));
    }

    @Test
    void testCreatePreference_Success() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(preferenceRepository.findByUser(user)).thenReturn(Optional.empty());
        when(preferenceMapper.toEntity(preferenceDTO, user)).thenReturn(preference);
        when(preferenceRepository.save(preference)).thenReturn(preference);
        when(preferenceMapper.toDto(preference)).thenReturn(preferenceDTO);

        PreferenceDTO result = preferenceService.createPreference(preferenceDTO);
        assertEquals(1L, result.getId());
    }

    @Test
    void testCreatePreference_AlreadyExists() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(preferenceRepository.findByUser(user)).thenReturn(Optional.of(preference));

        assertThrows(BadRequestException.class, () -> preferenceService.createPreference(preferenceDTO));
    }

    @Test
    void testUpdatePreference_Success() {
        when(preferenceRepository.findById(1L)).thenReturn(Optional.of(preference));
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(preferenceRepository.save(preference)).thenReturn(preference);
        when(preferenceMapper.toDto(preference)).thenReturn(preferenceDTO);

        PreferenceDTO result = preferenceService.updatePreference(1L, preferenceDTO);
        assertEquals(1L, result.getId());
    }

    @Test
    void testUpdatePreference_NotFound() {
        when(preferenceRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> preferenceService.updatePreference(1L, preferenceDTO));
    }

    @Test
    void testDeletePreference() {
        doNothing().when(preferenceRepository).deleteById(1L);
        preferenceService.deletePreference(1L);
        verify(preferenceRepository, times(1)).deleteById(1L);
    }
}
