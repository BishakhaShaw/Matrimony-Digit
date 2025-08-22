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
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final JwtUtil jwtUtil;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    // --- Get All Users (Admin / Manager only) ---
    public List<UserDTO> getAllUsers(Long requesterId) {
        User requester = getUserOrThrow(requesterId);

        if (!isAdmin(requester) && !isManager(requester)) {
            throw new BadRequestException("Only Admin or Manager can view all users");
        }

        return userRepository.findAll().stream()
                .map(UserMapper::toDTO)
                .collect(Collectors.toList());
    }

    // --- Get User by ID ---
    public UserDTO getUserById(Long requesterId, Long targetId) {
        User requester = getUserOrThrow(requesterId);
        User target = getUserOrThrow(targetId);

        if (isAdmin(requester) || isManager(requester) || requester.getId().equals(target.getId())) {
            return UserMapper.toDTO(target);
        }
        throw new BadRequestException("Access denied: You cannot view other users' details");
    }

    // --- Get My Profile ---
    public UserDTO getMyProfile(Long id) {
        return UserMapper.toDTO(getUserOrThrow(id));
    }

    // --- Create User (Main User OR Family Member) ---
    public UserDTO createUser(UserDTO dto) {
        // --- Check uniqueness ---
        if (userRepository.existsByUsername(dto.getUsername())) {
            throw new BadRequestException("Username already exists: " + dto.getUsername());
        }
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new BadRequestException("Email already exists: " + dto.getEmail());
        }

        // --- Enforce subscription type ---
        if (dto.getSubscriptionType() == null) {
            dto.setSubscriptionType("N");  // default
        } else if (!"N".equalsIgnoreCase(dto.getSubscriptionType())) {
            throw new BadRequestException("You can create account as a normal user only. Upgrade after account creation.");
        }

        // --- Assign Role ---
        Role role;
        if (dto.getRoleId() != null) {
            role = roleRepository.findById(dto.getRoleId())
                    .orElseThrow(() -> new ResourceNotFoundException("Role not found with id: " + dto.getRoleId()));

            if (isAdminRole(role) && userRepository.existsByRole(role)) {
                throw new BadRequestException("An Admin already exists. Only one Admin allowed.");
            }
            if (isManagerRole(role)) {
                throw new BadRequestException("Managers must be approved by Admin.");
            }
        } else {
            role = roleRepository.findByNameIgnoreCase("USER")
                    .orElseThrow(() -> new ResourceNotFoundException("Default role USER not found"));
        }

        // --- Handle Family Linking ---
        User linkedUser = null;
        if (dto.getIsFamilyMember() != null && dto.getIsFamilyMember()) {
            if (dto.getLinkedUserId() == null) {
                throw new BadRequestException("Family member must have a linkedUserId");
            }
            linkedUser = userRepository.findById(dto.getLinkedUserId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Linked User not found with id: " + dto.getLinkedUserId()));
        }

        // --- Map DTO -> Entity ---
        User user = UserMapper.toEntity(dto, role, linkedUser);

        // --- Handle Password ---
        String plainPassword = dto.getPassword();
        if (plainPassword == null || plainPassword.isBlank()) {
            plainPassword = generateSecurePassword();
            dto.setGeneratedPassword(plainPassword);
        }

        String salt = generateSalt();
        user.setPasswordSalt(salt);
        user.setPasswordHash(passwordEncoder.encode(plainPassword + salt));

        user = userRepository.save(user);

        // --- Map back to DTO ---
        UserDTO responseDto = UserMapper.toDTO(user);
        responseDto.setGeneratedPassword(dto.getGeneratedPassword());
        return responseDto;
    }

    // --- Get User by Username ---
    public UserDTO getUserByUsername(String username) {
        User user = userRepository.findByUsernameIgnoreCase(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with username: " + username));
        return UserMapper.toDTO(user);
    }

    // --- Update User (Self only) ---
    public UserDTO updateUser(Long requesterId, Long userId, UserDTO dto) {
        User requester = getUserOrThrow(requesterId);
        User existingUser = getUserOrThrow(userId);

        if (!requester.getId().equals(existingUser.getId())) {
            throw new BadRequestException("Only the user can update their own profile");
        }

        if (dto.getUsername() != null && !dto.getUsername().equalsIgnoreCase(existingUser.getUsername())
                && userRepository.existsByUsername(dto.getUsername())) {
            throw new BadRequestException("Username already taken");
        }
        if (dto.getEmail() != null && !dto.getEmail().equalsIgnoreCase(existingUser.getEmail())
                && userRepository.existsByEmail(dto.getEmail())) {
            throw new BadRequestException("Email already in use");
        }

        // --- Prevent subscription type update here ---
        if (dto.getSubscriptionType() != null &&
                !dto.getSubscriptionType().equalsIgnoreCase(existingUser.getSubscriptionType())) {
            throw new BadRequestException("Subscription type cannot be changed here. Please use the upgrade flow.");
        }

        existingUser.setUsername(dto.getUsername());
        existingUser.setEmail(dto.getEmail());
        existingUser.setPermanentLocation(dto.getPermanentLocation());
        existingUser.setIsActive(dto.getIsActive());

        return UserMapper.toDTO(userRepository.save(existingUser));
    }

    // --- Delete User (Admin / Manager only) ---
    public void deleteUser(Long requesterId, Long userId) {
        User requester = getUserOrThrow(requesterId);
        User targetUser = getUserOrThrow(userId);

        if (!isAdmin(requester) && !isManager(requester)) {
            throw new BadRequestException("Only Admin or Manager can delete users");
        }

        if (isManager(requester) && (isAdmin(targetUser) || isManager(targetUser))) {
            throw new BadRequestException("Managers cannot delete Admins or other Managers");
        }

        userRepository.delete(targetUser);
    }

    // --- Approve Manager (Admin only) ---
    public UserDTO approveManager(Long adminId, Long userId) {
        User admin = getUserOrThrow(adminId);
        if (!isAdmin(admin)) throw new BadRequestException("Only Admin can approve a Manager");

        User user = getUserOrThrow(userId);
        Role managerRole = roleRepository.findByNameIgnoreCase("MANAGER")
                .orElseThrow(() -> new ResourceNotFoundException("Manager role not found"));

        user.setRole(managerRole);
        userRepository.save(user);

        return UserMapper.toDTO(user);
    }

    // --- Login ---
    public LoginResponse login(LoginRequest request) {
        if (request.getEmailOrUsername() == null || request.getPassword() == null) {
            throw new BadRequestException("Username/Email and password are required");
        }

        User user = userRepository.findByUsernameIgnoreCaseOrEmailIgnoreCase(
                        request.getEmailOrUsername(), request.getEmailOrUsername())
                .orElseThrow(() -> new BadRequestException("Invalid username/email or password"));

        boolean authenticated = passwordEncoder.matches(request.getPassword() + user.getPasswordSalt(), user.getPasswordHash());
        if (!authenticated) throw new BadRequestException("Invalid username/email or password");

        String roleName = user.getRole() != null ? "ROLE_" + user.getRole().getName().toUpperCase() : "ROLE_USER";
        String token = jwtUtil.generateToken(user.getUsername(), Collections.singletonList(roleName));

        return LoginResponse.builder()
                .message("Login successful")
                .userId(user.getId())
                .token(token)
                .expiresAt(jwtUtil.getExpiration(token))
                .build();
    }

    // --- Helpers ---
    private String generateSecurePassword() {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789@#$%";
        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 10; i++) sb.append(chars.charAt(random.nextInt(chars.length())));
        return sb.toString();
    }

    private String generateSalt() {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 16; i++) sb.append(chars.charAt(random.nextInt(chars.length())));
        return sb.toString();
    }

    private User getUserOrThrow(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
    }

    private boolean isAdmin(User user) {
        return user.getRole() != null && "ADMIN".equalsIgnoreCase(user.getRole().getName());
    }

    private boolean isManager(User user) {
        return user.getRole() != null && "MANAGER".equalsIgnoreCase(user.getRole().getName());
    }

    private boolean isAdminRole(Role role) {
        return role != null && "ADMIN".equalsIgnoreCase(role.getName());
    }

    private boolean isManagerRole(Role role) {
        return role != null && "MANAGER".equalsIgnoreCase(role.getName());
    }
}
