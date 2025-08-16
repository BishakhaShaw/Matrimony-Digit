//
//
//
//
//package digit.matrimony.service;
//
//import digit.matrimony.dto.LoginRequest;
//import digit.matrimony.dto.LoginResponse;
//import digit.matrimony.dto.UserDTO;
//import digit.matrimony.entity.Role;
//import digit.matrimony.entity.User;
//import digit.matrimony.exception.BadRequestException;
//import digit.matrimony.exception.ResourceNotFoundException;
//import digit.matrimony.mapper.UserMapper;
//import digit.matrimony.repository.RoleRepository;
//import digit.matrimony.repository.UserRepository;
//import lombok.RequiredArgsConstructor;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.stereotype.Service;
//
//import java.security.SecureRandom;
//import java.util.List;
//import java.util.stream.Collectors;

//@Service
//@RequiredArgsConstructor
//public class UserService {
//
//    private final UserRepository userRepository;
//    private final RoleRepository roleRepository;
//    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
//
//    // --- Get All Users ---
//    public List<UserDTO> getAllUsers() {
//        return userRepository.findAll().stream()
//                .map(UserMapper::toDTO)
//                .collect(Collectors.toList());
//    }
//
//    // --- Get User by ID ---
//    public UserDTO getUserById(Long id) {
//        User user = userRepository.findById(id)
//                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
//        return UserMapper.toDTO(user);
//    }
//
//    // --- Create User ---
//    public UserDTO createUser(UserDTO dto) {
//        // Check uniqueness
//        if (userRepository.existsByUsername(dto.getUsername())) {
//            throw new IllegalArgumentException("Username already exists: " + dto.getUsername());
//        }
//        if (userRepository.existsByEmail(dto.getEmail())) {
//            throw new IllegalArgumentException("Email already exists: " + dto.getEmail());
//        }
//
//        Role role = null;
//        if (dto.getRoleId() != null) {
//            role = roleRepository.findById(dto.getRoleId())
//                    .orElseThrow(() -> new ResourceNotFoundException("Role not found with id: " + dto.getRoleId()));
//        }
//
//        User linkedUser = null;
//        if (dto.getLinkedUserId() != null) {
//            linkedUser = userRepository.findById(dto.getLinkedUserId())
//                    .orElseThrow(() -> new ResourceNotFoundException("Linked User not found with id: " + dto.getLinkedUserId()));
//        }
//
//        User user = UserMapper.toEntity(dto, role, linkedUser);
//
//        // Handle password
//        String plainPassword = dto.getPassword();
//        if (plainPassword == null || plainPassword.isBlank()) {
//            plainPassword = generateSecurePassword();
//            dto.setGeneratedPassword(plainPassword);
//        }
//
//        // Combine password + salt before encoding
//        String salt = generateSalt();
//        user.setPasswordSalt(salt);
//        user.setPasswordHash(passwordEncoder.encode(plainPassword + salt));
//
//        user = userRepository.save(user);
//
//        UserDTO responseDto = UserMapper.toDTO(user);
//        responseDto.setGeneratedPassword(dto.getGeneratedPassword());
//        return responseDto;
//    }
//
//    // --- Update User ---
//    public UserDTO updateUser(Long id, UserDTO dto) {
//        User existingUser = userRepository.findById(id)
//                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
//
//        // Check uniqueness
//        if (!existingUser.getUsername().equals(dto.getUsername()) && userRepository.existsByUsername(dto.getUsername())) {
//            throw new IllegalArgumentException("Username already exists: " + dto.getUsername());
//        }
//        if (!existingUser.getEmail().equals(dto.getEmail()) && userRepository.existsByEmail(dto.getEmail())) {
//            throw new IllegalArgumentException("Email already exists: " + dto.getEmail());
//        }
//
//        existingUser.setUsername(dto.getUsername());
//        existingUser.setEmail(dto.getEmail());
//        existingUser.setPermanentLocation(dto.getPermanentLocation());
//        existingUser.setSubscriptionType(dto.getSubscriptionType());
//        existingUser.setIsActive(dto.getIsActive());
//
//        // Update password if provided
//        if (dto.getPassword() != null && !dto.getPassword().isBlank()) {
//            String salt = generateSalt();
//            existingUser.setPasswordSalt(salt);
//            existingUser.setPasswordHash(passwordEncoder.encode(dto.getPassword() + salt));
//        }
//
//        // Update role
//        if (dto.getRoleId() != null) {
//            Role role = roleRepository.findById(dto.getRoleId())
//                    .orElseThrow(() -> new ResourceNotFoundException("Role not found with id: " + dto.getRoleId()));
//            existingUser.setRole(role);
//        } else {
//            existingUser.setRole(null);
//        }
//
//        // Update linked user
//        if (dto.getLinkedUserId() != null) {
//            User linkedUser = userRepository.findById(dto.getLinkedUserId())
//                    .orElseThrow(() -> new ResourceNotFoundException("Linked User not found with id: " + dto.getLinkedUserId()));
//            existingUser.setLinkedUser(linkedUser);
//        } else {
//            existingUser.setLinkedUser(null);
//        }
//
//        existingUser = userRepository.save(existingUser);
//        return UserMapper.toDTO(existingUser);
//    }
//
//    // --- Login ---
//    public LoginResponse login(LoginRequest request) {
//        if (request.getEmail() == null || request.getPassword() == null) {
//            throw new BadRequestException("Email and password are required");
//        }
//
//        User user = userRepository.findByEmail(request.getEmail())
//                .orElseThrow(() -> new BadRequestException("Invalid email or password"));
//
//        // Check password by combining input + stored salt
//        if (!passwordEncoder.matches(request.getPassword() + user.getPasswordSalt(), user.getPasswordHash())) {
//            throw new BadRequestException("Invalid email or password");
//        }
//
//        return new LoginResponse("Login successful", user.getId());
//    }
//
//    // --- Helpers ---
//    private String generateSecurePassword() {
//        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789@#$%";
//        SecureRandom random = new SecureRandom();
//        StringBuilder sb = new StringBuilder();
//        for (int i = 0; i < 10; i++) {
//            sb.append(chars.charAt(random.nextInt(chars.length())));
//        }
//        return sb.toString();
//    }
//
//    private String generateSalt() {
//        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
//        SecureRandom random = new SecureRandom();
//        StringBuilder sb = new StringBuilder();
//        for (int i = 0; i < 16; i++) {
//            sb.append(chars.charAt(random.nextInt(chars.length())));
//        }
//        return sb.toString();
//    }
//}

























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
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    // --- Get All Users (only for Admin / Manager) ---
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
        } else {
            throw new BadRequestException("Access denied: You cannot view other users' details");
        }
    }

    // --- Get My Profile (Self only) ---
    public UserDTO getMyProfile(Long id) {
        User user = getUserOrThrow(id);
        return UserMapper.toDTO(user);
    }

    // --- Create User ---
    public UserDTO createUser(UserDTO dto) {
        if (userRepository.existsByUsername(dto.getUsername())) {
            throw new IllegalArgumentException("Username already exists: " + dto.getUsername());
        }
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new IllegalArgumentException("Email already exists: " + dto.getEmail());
        }

        Role role = null;
        if (dto.getRoleId() != null) {
            role = roleRepository.findById(dto.getRoleId())
                    .orElseThrow(() -> new ResourceNotFoundException("Role not found with id: " + dto.getRoleId()));

            if (isAdminRole(role) && userRepository.existsByRole(role)) {
                throw new BadRequestException("An Admin already exists. Only one Admin is allowed.");
            }

            if (isManagerRole(role)) {
                throw new BadRequestException("You cannot create a Manager directly. Only Admin can approve a Manager.");
            }
        }

        User linkedUser = null;
        if (dto.getLinkedUserId() != null) {
            linkedUser = userRepository.findById(dto.getLinkedUserId())
                    .orElseThrow(() -> new ResourceNotFoundException("Linked User not found with id: " + dto.getLinkedUserId()));
        }

        User user = UserMapper.toEntity(dto, role, linkedUser);

        String plainPassword = dto.getPassword();
        if (plainPassword == null || plainPassword.isBlank()) {
            plainPassword = generateSecurePassword();
            dto.setGeneratedPassword(plainPassword);
        }

        String salt = generateSalt();
        user.setPasswordSalt(salt);
        user.setPasswordHash(passwordEncoder.encode(plainPassword + salt));

        user = userRepository.save(user);

        UserDTO responseDto = UserMapper.toDTO(user);
        responseDto.setGeneratedPassword(dto.getGeneratedPassword());
        return responseDto;
    }

    // --- Update User (Self only) ---
    public UserDTO updateUser(Long requesterId, Long userId, UserDTO dto) {
        User requester = getUserOrThrow(requesterId);
        User existingUser = getUserOrThrow(userId);

        // Only self-update allowed
        if (!requester.getId().equals(existingUser.getId())) {
            throw new BadRequestException("Access denied: Only the user can update their own profile");
        }

        existingUser.setUsername(dto.getUsername());
        existingUser.setEmail(dto.getEmail());
        existingUser.setPermanentLocation(dto.getPermanentLocation());
        existingUser.setSubscriptionType(dto.getSubscriptionType());
        existingUser.setIsActive(dto.getIsActive());

        // Role or linked user cannot be updated here

        User saved = userRepository.save(existingUser);
        return UserMapper.toDTO(saved);
    }

    // --- Delete User (Admin / Manager only) ---
    public void deleteUser(Long requesterId, Long userId) {
        User requester = getUserOrThrow(requesterId);
        User targetUser = getUserOrThrow(userId);

        if (!isAdmin(requester) && !isManager(requester)) {
            throw new BadRequestException("Only Admin or Manager can delete users");
        }

        // Managers cannot delete Admins or other Managers
        if (isManager(requester) && (isAdmin(targetUser) || isManager(targetUser))) {
            throw new BadRequestException("Managers cannot delete Admins or other Managers");
        }

        userRepository.delete(targetUser);
    }

    // --- Approve Manager (Only Admin) ---
    public UserDTO approveManager(Long adminId, Long userId) {
        User admin = getUserOrThrow(adminId);
        if (!isAdmin(admin)) {
            throw new BadRequestException("Only Admin can approve a Manager");
        }

        User user = getUserOrThrow(userId);
        Role managerRole = roleRepository.findById((short) 2)
                .orElseThrow(() -> new ResourceNotFoundException("Manager role not found"));

        user.setRole(managerRole);
        userRepository.save(user);

        return UserMapper.toDTO(user);
    }

    // --- Login (username OR email) ---
    public LoginResponse login(LoginRequest request) {
        if (request.getEmailOrUsername() == null || request.getPassword() == null) {
            throw new BadRequestException("Username/Email and password are required");
        }

        User user = userRepository.findByUsernameIgnoreCaseOrEmailIgnoreCase(
                        request.getEmailOrUsername(), request.getEmailOrUsername())
                .orElseThrow(() -> new BadRequestException("Invalid username/email or password"));

        if (!passwordEncoder.matches(request.getPassword() + user.getPasswordSalt(), user.getPasswordHash())) {
            throw new BadRequestException("Invalid username/email or password");
        }

        return new LoginResponse("Login successful", user.getId());
    }

    // --- Helpers ---
    private String generateSecurePassword() {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789@#$%";
        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 10; i++) {
            sb.append(chars.charAt(random.nextInt(chars.length())));
        }
        return sb.toString();
    }

    private String generateSalt() {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 16; i++) {
            sb.append(chars.charAt(random.nextInt(chars.length())));
        }
        return sb.toString();
    }

    private User getUserOrThrow(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
    }

    private boolean isAdmin(User user) {
        return user.getRole() != null && user.getRole().getId() == (short) 1;
    }

    private boolean isManager(User user) {
        return user.getRole() != null && user.getRole().getId() == (short) 2;
    }

    private boolean isAdminRole(Role role) {
        return role.getId() == (short) 1;
    }

    private boolean isManagerRole(Role role) {
        return role.getId() == (short) 2;
    }
}
