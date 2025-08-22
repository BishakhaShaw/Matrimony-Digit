package digit.matrimony.service;

import digit.matrimony.dto.ProfileDTO;
import digit.matrimony.dto.ProfilePhotoDTO;
import digit.matrimony.entity.Profile;
import digit.matrimony.entity.ProfilePhoto;
import digit.matrimony.entity.User;
import digit.matrimony.exception.BadRequestException;
import digit.matrimony.exception.ResourceNotFoundException;
import digit.matrimony.mapper.ProfileMapper;
import digit.matrimony.mapper.ProfilePhotoMapper;
import digit.matrimony.repository.ProfileRepository;
import digit.matrimony.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProfileServiceTest {

    @InjectMocks
    private ProfileService profileService;

    @Mock
    private ProfileRepository profileRepository;

    @Mock
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    private ProfileDTO createValidProfileDTO() {
        return ProfileDTO.builder()
                .userId(1L)
                .gender("Male")
                .dateOfBirth(LocalDateTime.of(1995, 1, 1, 0, 0))
                .phone("1234567890")
                .aadhaarNumber("123412341234")
                .bio("Test bio")
                .religion("Hindu")
                .caste("Brahmin")
                .education("B.Tech")
                .occupation("Engineer")
                .income("10LPA")
                .currentLocation("Mumbai")
                .height(BigDecimal.valueOf(5.9))
                .maritalStatus("Single")
                .isVerified(true)
                .photos(List.of(
                        ProfilePhotoDTO.builder().photoUrl("url1").isProfilePhoto(true).uploadedAt(LocalDateTime.now()).build(),
                        ProfilePhotoDTO.builder().photoUrl("url2").isProfilePhoto(false).uploadedAt(LocalDateTime.now()).build()
                ))
                .build();
    }

    @Test
    void testGetAllProfiles() {
        Profile profile = new Profile();
        when(profileRepository.findAll()).thenReturn(List.of(profile));
        ProfileDTO dto = ProfileMapper.toDTO(profile);

        List<ProfileDTO> result = profileService.getAllProfiles();
        assertNotNull(result);
        assertFalse(result.isEmpty());
    }

    @Test
    void testGetProfileById_Success() {
        Profile profile = new Profile();
        profile.setId(1L);
        when(profileRepository.findById(1L)).thenReturn(Optional.of(profile));

        ProfileDTO dto = profileService.getProfileById(1L);
        assertEquals(1L, dto.getId());
    }

    @Test
    void testGetProfileById_NotFound() {
        when(profileRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> profileService.getProfileById(1L));
    }

    @Test
    void testCreateProfile_Success() {
        ProfileDTO dto = createValidProfileDTO();
        User user = new User();
        user.setId(1L);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(profileRepository.save(any(Profile.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ProfileDTO result = profileService.createProfile(dto);
        assertNotNull(result);
        verify(profileRepository).save(any(Profile.class));
    }

    @Test
    void testCreateProfile_MissingUserId() {
        ProfileDTO dto = createValidProfileDTO();
        dto.setUserId(null);
        assertThrows(BadRequestException.class, () -> profileService.createProfile(dto));
    }

    @Test
    void testCreateProfile_InvalidPhotos() {
        ProfileDTO dto = createValidProfileDTO();
        dto.setPhotos(List.of(ProfilePhotoDTO.builder().photoUrl("url1").isProfilePhoto(false).build()));
        assertThrows(ResourceNotFoundException.class, () -> profileService.createProfile(dto));
    }

    @Test
    void testUpdateProfile_Success() {
        ProfileDTO dto = createValidProfileDTO();
        Profile existing = new Profile();
        existing.setId(1L);

        when(profileRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(userRepository.findById(1L)).thenReturn(Optional.of(new User()));
        when(profileRepository.save(any(Profile.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ProfileDTO result = profileService.updateProfile(1L, dto);
        assertNotNull(result);
        verify(profileRepository).save(existing);
    }

    @Test
    void testUpdateProfile_NotFound() {
        when(profileRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> profileService.updateProfile(1L, createValidProfileDTO()));
    }

    @Test
    void testDeleteProfile_Success() {
        Profile profile = new Profile();
        profile.setId(1L);
        when(profileRepository.findById(1L)).thenReturn(Optional.of(profile));

        profileService.deleteProfile(1L);
        verify(profileRepository).delete(profile);
    }

    @Test
    void testDeleteProfile_NotFound() {
        when(profileRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> profileService.deleteProfile(1L));
    }
}
