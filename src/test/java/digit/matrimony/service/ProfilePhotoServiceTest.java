package digit.matrimony.service;

import digit.matrimony.dto.ProfilePhotoDTO;
import digit.matrimony.entity.Profile;
import digit.matrimony.entity.ProfilePhoto;
import digit.matrimony.exception.BadRequestException;
import digit.matrimony.exception.ResourceNotFoundException;
import digit.matrimony.mapper.ProfilePhotoMapper;
import digit.matrimony.repository.ProfilePhotoRepository;
import digit.matrimony.repository.ProfileRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProfilePhotoServiceTest {

    @InjectMocks
    private ProfilePhotoService profilePhotoService;

    @Mock
    private ProfilePhotoRepository profilePhotoRepository;

    @Mock
    private ProfileRepository profileRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllProfilePhotos() {
        ProfilePhoto photo = ProfilePhoto.builder()
                .id(1L)
                .photoUrl("http://example.com/photo.jpg")
                .isProfilePhoto(true)
                .build();

        when(profilePhotoRepository.findAll()).thenReturn(List.of(photo));


        List<ProfilePhotoDTO> result = profilePhotoService.getAllProfilePhotos();
        assertEquals(1, result.size());
        assertEquals("http://example.com/photo.jpg", result.get(0).getPhotoUrl());
    }

    @Test
    void testGetProfilePhotoById_Success() {
        ProfilePhoto photo = ProfilePhoto.builder().id(1L).photoUrl("url").build();
        when(profilePhotoRepository.findById(1L)).thenReturn(Optional.of(photo));

        ProfilePhotoDTO result = profilePhotoService.getProfilePhotoById(1L);
        assertEquals(1L, result.getId());
    }

    @Test
    void testGetProfilePhotoById_NotFound() {
        when(profilePhotoRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> profilePhotoService.getProfilePhotoById(1L));
    }

    @Test
    void testCreateProfilePhoto_Success() {
        Profile profile = Profile.builder().id(1L).build();
        ProfilePhotoDTO dto = ProfilePhotoDTO.builder()
                .profileId(1L)
                .photoUrl("url")
                .isProfilePhoto(true)
                .build();

        ProfilePhoto photo = ProfilePhotoMapper.toEntity(dto);
        photo.setProfile(profile);

        when(profileRepository.findById(1L)).thenReturn(Optional.of(profile));
        when(profilePhotoRepository.save(any(ProfilePhoto.class))).thenReturn(photo);

        ProfilePhotoDTO result = profilePhotoService.createProfilePhoto(dto);
        assertEquals("url", result.getPhotoUrl());
    }

    @Test
    void testCreateProfilePhoto_MissingProfileId() {
        ProfilePhotoDTO dto = ProfilePhotoDTO.builder().photoUrl("url").build();
        assertThrows(BadRequestException.class, () -> profilePhotoService.createProfilePhoto(dto));
    }

    @Test
    void testUpdateProfilePhoto_Success() {
        ProfilePhoto existing = ProfilePhoto.builder().id(1L).photoUrl("old").build();
        ProfilePhotoDTO dto = ProfilePhotoDTO.builder().photoUrl("new").isProfilePhoto(true).build();

        when(profilePhotoRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(profilePhotoRepository.save(any(ProfilePhoto.class))).thenReturn(existing);

        ProfilePhotoDTO result = profilePhotoService.updateProfilePhoto(1L, dto);
        assertEquals("new", result.getPhotoUrl());
    }

    @Test
    void testDeleteProfilePhoto_Success() {
        ProfilePhoto photo = ProfilePhoto.builder().id(1L).build();
        when(profilePhotoRepository.findById(1L)).thenReturn(Optional.of(photo));

        profilePhotoService.deleteProfilePhoto(1L);
        verify(profilePhotoRepository, times(1)).delete(photo);
    }

    @Test
    void testDeleteProfilePhoto_NotFound() {
        when(profilePhotoRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> profilePhotoService.deleteProfilePhoto(1L));
    }
}
