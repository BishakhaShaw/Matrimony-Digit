package digit.matrimony.service;

import digit.matrimony.dto.LoginRequest;
import digit.matrimony.dto.LoginResponse;
import digit.matrimony.dto.UserDTO;
import digit.matrimony.entity.Role;
import digit.matrimony.entity.User;
import digit.matrimony.exception.BadRequestException;
import digit.matrimony.exception.ResourceNotFoundException;
import digit.matrimony.mapper.UserMapper;
import digit.matrimony.repository.RoleRepository;
import digit.matrimony.repository.UserRepository;
import digit.matrimony.security.jwt.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @InjectMocks
    private UserService userService; // Uses @RequiredArgsConstructor in service

    @Mock private UserRepository userRepository;
    @Mock private RoleRepository roleRepository;
    @Mock private JwtUtil jwtUtil;

    // --- Helpers ----

    private Role role(String name) {
        Role r = new Role();
        r.setId((short) new Random().nextLong());
        r.setName(name);
        return r;
    }

    private User user(Long id, String username, String email, Role role) {
        return User.builder()
                .id(id)
                .username(username)
                .email(email)
                .role(role)
                .isActive(true)
                .subscriptionType("N")
                .build();
    }

    private UserDTO dto(Long id, String username, String email) {
        UserDTO d = new UserDTO();
        d.setId(id);
        d.setUsername(username);
        d.setEmail(email);
        return d;
    }

    private void stubUserMapperDefault(MockedStatic<UserMapper> mocked) {
        // Generic toDTO: copy id/username/email for assertions
        mocked.when(() -> UserMapper.toDTO(any(User.class))).thenAnswer(inv -> {
            User u = inv.getArgument(0);
            UserDTO d = new UserDTO();
            d.setId(u.getId());
            d.setUsername(u.getUsername());
            d.setEmail(u.getEmail());
            d.setIsActive(u.getIsActive());
            d.setSubscriptionType(u.getSubscriptionType());
            return d;
        });

        // Generic toEntity: use username/email/role/linkedUser from args
        mocked.when(() -> UserMapper.toEntity(any(UserDTO.class), any(Role.class), any(User.class)))
                .thenAnswer(inv -> {
                    UserDTO d = inv.getArgument(0);
                    Role r = inv.getArgument(1);
                    User linked = inv.getArgument(2);
                    return User.builder()
                            .id(d.getId())
                            .username(d.getUsername())
                            .email(d.getEmail())
                            .role(r)
                            .linkedUser(linked)
                            .isFamilyMember(Boolean.TRUE.equals(d.getIsFamilyMember()))
                            .subscriptionType(Objects.requireNonNullElse(d.getSubscriptionType(), "N"))
                            .isActive(d.getIsActive())
                            .build();
                });
    }

    // --- Tests ----

    @Test
    void getAllUsers_asAdmin_returnsList() {
        Role adminRole = role("ADMIN");
        User admin = user(1L, "admin", "a@a.com", adminRole);
        when(userRepository.findById(1L)).thenReturn(Optional.of(admin));

        User u1 = user(2L, "u1", "u1@x.com", role("USER"));
        User u2 = user(3L, "u2", "u2@x.com", role("USER"));
        when(userRepository.findAll()).thenReturn(List.of(u1, u2));

        try (MockedStatic<UserMapper> mocked = mockStatic(UserMapper.class)) {
            stubUserMapperDefault(mocked);

            List<UserDTO> result = userService.getAllUsers(1L);
            assertEquals(2, result.size());
            assertEquals("u1", result.get(0).getUsername());
            assertEquals("u2", result.get(1).getUsername());
        }
    }

    @Test
    void getAllUsers_asRegularUser_throws() {
        Role userRole = role("USER");
        when(userRepository.findById(10L)).thenReturn(Optional.of(user(10L, "x", "x@x.com", userRole)));
        assertThrows(BadRequestException.class, () -> userService.getAllUsers(10L));
    }

    @Test
    void getUserById_self_ok() {
        Role userRole = role("USER");
        User self = user(5L, "me", "me@x.com", userRole);
        when(userRepository.findById(5L)).thenReturn(Optional.of(self));

        try (MockedStatic<UserMapper> mocked = mockStatic(UserMapper.class)) {
            stubUserMapperDefault(mocked);
            UserDTO dto = userService.getUserById(5L, 5L);
            assertEquals("me", dto.getUsername());
        }
    }

    @Test
    void getUserById_other_asManager_ok() {
        Role managerRole = role("MANAGER");
        User manager = user(1L, "mgr", "m@x.com", managerRole);
        User target = user(2L, "user", "u@x.com", role("USER"));

        when(userRepository.findById(1L)).thenReturn(Optional.of(manager));
        when(userRepository.findById(2L)).thenReturn(Optional.of(target));

        try (MockedStatic<UserMapper> mocked = mockStatic(UserMapper.class)) {
            stubUserMapperDefault(mocked);
            UserDTO dto = userService.getUserById(1L, 2L);
            assertEquals("user", dto.getUsername());
        }
    }

    @Test
    void getUserById_other_asUser_throws() {
        Role userRole = role("USER");
        when(userRepository.findById(1L)).thenReturn(Optional.of(user(1L, "a", "a@x.com", userRole)));
        when(userRepository.findById(2L)).thenReturn(Optional.of(user(2L, "b", "b@x.com", userRole)));
        assertThrows(BadRequestException.class, () -> userService.getUserById(1L, 2L));
    }

    @Test
    void getMyProfile_ok() {
        Role userRole = role("USER");
        User me = user(7L, "me", "me@x.com", userRole);
        when(userRepository.findById(7L)).thenReturn(Optional.of(me));

        try (MockedStatic<UserMapper> mocked = mockStatic(UserMapper.class)) {
            stubUserMapperDefault(mocked);
            UserDTO dto = userService.getMyProfile(7L);
            assertEquals("me", dto.getUsername());
        }
    }

    @Test
    void createUser_withManagerRoleId_rejectedUntilAdminApproval() {
        UserDTO req = new UserDTO();
        req.setUsername("mgr");
        req.setEmail("m@x.com");
        req.setRoleId((short) 99L);

        when(userRepository.existsByUsername("mgr")).thenReturn(false);
        when(userRepository.existsByEmail("m@x.com")).thenReturn(false);

        Role managerRole = role("MANAGER");
        when(roleRepository.findById((short) 99L)).thenReturn(Optional.of(managerRole));

        assertThrows(BadRequestException.class, () -> userService.createUser(req));
        verify(userRepository, never()).save(any());
    }

    @Test
    void createUser_adminRole_whenAdminAlreadyExists_rejected() {
        UserDTO req = new UserDTO();
        req.setUsername("admin2");
        req.setEmail("a2@x.com");
        req.setRoleId((short) 1L);

        when(userRepository.existsByUsername("admin2")).thenReturn(false);
        when(userRepository.existsByEmail("a2@x.com")).thenReturn(false);

        Role adminRole = role("ADMIN");
        when(roleRepository.findById((short) 1L)).thenReturn(Optional.of(adminRole));
        when(userRepository.existsByRole(adminRole)).thenReturn(true);

        assertThrows(BadRequestException.class, () -> userService.createUser(req));
    }

    @Test
    void createUser_familyMember_withoutLinkedUser_throws() {
        UserDTO req = new UserDTO();
        req.setUsername("fam");
        req.setEmail("fam@x.com");
        req.setIsFamilyMember(true);           // triggers family check
        req.setLinkedUserId(null);             // no linked user -> should throw BadRequestException

        when(userRepository.existsByUsername("fam")).thenReturn(false);
        when(userRepository.existsByEmail("fam@x.com")).thenReturn(false);

        // 🔧 IMPORTANT: stub default role lookup so we reach the family member check
        when(roleRepository.findByNameIgnoreCase("USER"))
                .thenReturn(Optional.of(role("USER")));

        assertThrows(BadRequestException.class, () -> userService.createUser(req));
    }

    @Test
    void getUserByUsername_ok() {
        User u = user(20L, "dax", "d@x.com", role("USER"));
        when(userRepository.findByUsernameIgnoreCase("dax")).thenReturn(Optional.of(u));

        try (MockedStatic<UserMapper> mocked = mockStatic(UserMapper.class)) {
            stubUserMapperDefault(mocked);
            UserDTO res = userService.getUserByUsername("dax");
            assertEquals("dax", res.getUsername());
        }
    }

    @Test
    void getUserByUsername_notFound_throws() {
        when(userRepository.findByUsernameIgnoreCase("ghost")).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> userService.getUserByUsername("ghost"));
    }

    @Test
    void updateUser_self_ok() {
        User existing = user(11L, "me", "old@x.com", role("USER"));
        when(userRepository.findById(11L)).thenReturn(Optional.of(existing));
        when(userRepository.save(any(User.class))).thenAnswer(inv -> inv.getArgument(0));

        UserDTO req = new UserDTO();
        req.setUsername("me");
        req.setEmail("new@x.com");
        req.setPermanentLocation("Goa");
        req.setSubscriptionType("P");
        req.setIsActive(true);

        try (MockedStatic<UserMapper> mocked = mockStatic(UserMapper.class)) {
            stubUserMapperDefault(mocked);
            UserDTO res = userService.updateUser(11L, 11L, req);
            assertEquals("new@x.com", res.getEmail());
            verify(userRepository).save(any(User.class));
        }
    }

    @Test
    void updateUser_notSelf_throws() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user(1L, "a", "a@x.com", role("USER"))));
        when(userRepository.findById(2L)).thenReturn(Optional.of(user(2L, "b", "b@x.com", role("USER"))));
        assertThrows(BadRequestException.class, () -> userService.updateUser(1L, 2L, new UserDTO()));
    }

    @Test
    void deleteUser_asAdmin_ok() {
        User admin = user(1L, "admin", "a@x.com", role("ADMIN"));
        User target = user(2L, "victim", "v@x.com", role("USER"));
        when(userRepository.findById(1L)).thenReturn(Optional.of(admin));
        when(userRepository.findById(2L)).thenReturn(Optional.of(target));

        assertDoesNotThrow(() -> userService.deleteUser(1L, 2L));
        verify(userRepository).delete(target);
    }

    @Test
    void deleteUser_managerCannotDeleteAdmin_throws() {
        User manager = user(1L, "mgr", "m@x.com", role("MANAGER"));
        User admin = user(2L, "admin", "a@x.com", role("ADMIN"));
        when(userRepository.findById(1L)).thenReturn(Optional.of(manager));
        when(userRepository.findById(2L)).thenReturn(Optional.of(admin));

        assertThrows(BadRequestException.class, () -> userService.deleteUser(1L, 2L));
        verify(userRepository, never()).delete(any());
    }

    @Test
    void approveManager_byAdmin_ok() {
        User admin = user(1L, "admin", "a@x.com", role("ADMIN"));
        User candidate = user(2L, "cand", "c@x.com", role("USER"));
        Role managerRole = role("MANAGER");

        when(userRepository.findById(1L)).thenReturn(Optional.of(admin));
        when(userRepository.findById(2L)).thenReturn(Optional.of(candidate));
        when(roleRepository.findByNameIgnoreCase("MANAGER")).thenReturn(Optional.of(managerRole));
        when(userRepository.save(any(User.class))).thenAnswer(inv -> inv.getArgument(0));

        try (MockedStatic<UserMapper> mocked = mockStatic(UserMapper.class)) {
            stubUserMapperDefault(mocked);
            UserDTO res = userService.approveManager(1L, 2L);
            assertEquals("cand", res.getUsername());
            assertEquals("c@x.com", res.getEmail());
        }
    }

    @Test
    void approveManager_notAdmin_throws() {
        User notAdmin = user(1L, "u", "u@x.com", role("USER"));
        when(userRepository.findById(1L)).thenReturn(Optional.of(notAdmin));
        assertThrows(BadRequestException.class, () -> userService.approveManager(1L, 2L));
    }

    @Test
    void login_valid_ok() {
        LoginRequest req = new LoginRequest();
        req.setEmailOrUsername("user1");
        req.setPassword("pass123");

        User u = user(5L, "user1", "u@x.com", role("USER"));
        u.setPasswordSalt("salt");
        // Service creates its own BCryptPasswordEncoder; replicate the same hashing
        u.setPasswordHash(new BCryptPasswordEncoder().encode("pass123" + "salt"));

        when(userRepository.findByUsernameIgnoreCaseOrEmailIgnoreCase("user1", "user1"))
                .thenReturn(Optional.of(u));
        when(jwtUtil.generateToken(eq("user1"), anyList())).thenReturn("jwt-token");
        when(jwtUtil.getExpiration("jwt-token")).thenReturn(new Date(System.currentTimeMillis() + 3600_000));

        LoginResponse res = userService.login(req);
        assertEquals("Login successful", res.getMessage());
        assertEquals(5L, res.getUserId());
        assertEquals("jwt-token", res.getToken());
        assertNotNull(res.getExpiresAt());
    }

    @Test
    void login_invalidPassword_throws() {
        LoginRequest req = new LoginRequest();
        req.setEmailOrUsername("user1");
        req.setPassword("wrong");

        User u = user(5L, "user1", "u@x.com", role("USER"));
        u.setPasswordSalt("salt");
        u.setPasswordHash(new BCryptPasswordEncoder().encode("correct" + "salt"));

        when(userRepository.findByUsernameIgnoreCaseOrEmailIgnoreCase("user1", "user1"))
                .thenReturn(Optional.of(u));

        assertThrows(BadRequestException.class, () -> userService.login(req));
    }

    @Test
    void login_missingCredentials_throws() {
        LoginRequest req = new LoginRequest();
        req.setEmailOrUsername(null);
        req.setPassword(null);
        assertThrows(BadRequestException.class, () -> userService.login(req));
    }
}
