

package digit.matrimony.controller;

import digit.matrimony.dto.LoginRequest;
import digit.matrimony.dto.LoginResponse;
import digit.matrimony.dto.UserDTO;
import digit.matrimony.security.jwt.JwtUtil;
import digit.matrimony.security.logout.TokenBlacklistService;
import digit.matrimony.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final JwtUtil jwtUtil;
    private final TokenBlacklistService tokenBlacklistService;

    // --- Get All Users (Admin / Manager only) ---
    @GetMapping("/all")
    public ResponseEntity<List<UserDTO>> getAllUsers(HttpServletRequest request) {
        Long requesterId = extractUserIdFromToken(request);
        return ResponseEntity.ok(userService.getAllUsers(requesterId));
    }

    // --- Get User by ID ---
    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long id, HttpServletRequest request) {
        Long requesterId = extractUserIdFromToken(request);
        return ResponseEntity.ok(userService.getUserById(requesterId, id));
    }

    // --- Get My Profile ---
    @GetMapping("/me")
    public ResponseEntity<UserDTO> getMyProfile(HttpServletRequest request) {
        Long requesterId = extractUserIdFromToken(request);
        return ResponseEntity.ok(userService.getMyProfile(requesterId));
    }

    // --- Create User (Public) ---
    @PostMapping
    public ResponseEntity<UserDTO> createUser(@Valid @RequestBody UserDTO dto) {
        // Service layer will enforce:
        // - If role is FAMILY_MEMBER => linkedUserId must be provided
        // - If not FAMILY_MEMBER => linkedUserId must be null
        UserDTO created = userService.createUser(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    // --- Update User (Self only) ---
    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> updateUser(@PathVariable Long id,
                                              @Valid @RequestBody UserDTO dto,
                                              HttpServletRequest request) {
        Long requesterId = extractUserIdFromToken(request);
        return ResponseEntity.ok(userService.updateUser(requesterId, id, dto));
    }

    // --- Delete User (Admin / Manager only) ---
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteUser(@PathVariable Long id, HttpServletRequest request) {
        Long requesterId = extractUserIdFromToken(request);
        userService.deleteUser(requesterId, id);
        return ResponseEntity.ok(Map.of("message", "User deleted successfully"));
    }

    // --- Approve Manager (Admin only) ---
    @PostMapping("/approve-manager/{id}")
    public ResponseEntity<UserDTO> approveManager(@PathVariable Long id, HttpServletRequest request) {
        Long requesterId = extractUserIdFromToken(request);
        return ResponseEntity.ok(userService.approveManager(requesterId, id));
    }

    // --- Login ---
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(userService.login(request));
    }

    // --- Logout ---
    @PostMapping("/logout")
    public ResponseEntity<Map<String, String>> logout(HttpServletRequest request) {
        String auth = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (auth != null && auth.startsWith("Bearer ")) {
            String token = auth.substring(7);
            tokenBlacklistService.blacklist(token);
        }
        return ResponseEntity.ok(Map.of("message", "Logged out"));
    }

    // --- Helper: Extract User ID from JWT ---
    private Long extractUserIdFromToken(HttpServletRequest request) {
        String auth = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (auth == null || !auth.startsWith("Bearer ")) {
            throw new RuntimeException("Authorization header missing or invalid");
        }
        String token = auth.substring(7);
        String username = jwtUtil.getSubject(token);
        // Service method resolves username -> user -> ID
        return userService.getUserByUsername(username).getId();
    }
}
