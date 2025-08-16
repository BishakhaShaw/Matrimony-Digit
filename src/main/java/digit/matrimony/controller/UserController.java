//
//package digit.matrimony.controller;
//
//import digit.matrimony.dto.LoginRequest;
//import digit.matrimony.dto.LoginResponse;
//import digit.matrimony.dto.UserDTO;
//import digit.matrimony.service.UserService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import jakarta.validation.Valid;
//import java.util.List;
//
//@RestController
//@RequestMapping("/api/users")
//@RequiredArgsConstructor
//public class UserController {
//
//    private final UserService userService;
//
//    @GetMapping
//    public ResponseEntity<List<UserDTO>> getAllUsers() {
//        return ResponseEntity.ok(userService.getAllUsers());
//    }
//
//    @GetMapping("/{id}")
//    public ResponseEntity<UserDTO> getUserById(@PathVariable Long id) {
//        return ResponseEntity.ok(userService.getUserById(id));
//    }
//
//    @PostMapping
//    public ResponseEntity<UserDTO> createUser(@Valid @RequestBody UserDTO dto) {
//        UserDTO created = userService.createUser(dto);
//        return ResponseEntity.status(HttpStatus.CREATED).body(created);
//    }
//
//    @PutMapping("/{id}")
//    public ResponseEntity<UserDTO> updateUser(@PathVariable Long id, @Valid @RequestBody UserDTO dto) {
//        return ResponseEntity.ok(userService.updateUser(id, dto));
//    }
//
//    // --- Login endpoint ---
//    @PostMapping("/login")
//    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
//        LoginResponse response = userService.login(request);
//        return ResponseEntity.ok(response);
//    }
//}











































package digit.matrimony.controller;

import digit.matrimony.dto.LoginRequest;
import digit.matrimony.dto.LoginResponse;
import digit.matrimony.dto.UserDTO;
import digit.matrimony.service.UserService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // ✅ Helper DTOs for request bodies
    @Data
    public static class RequesterDTO {
        private Long requesterId;
    }

    @Data
    public static class UserActionRequest {
        private Long requesterId;
        private Long targetUserId;
    }

    @Data
    public static class UpdateUserRequest {
        private Long requesterId;
        private UserDTO user;
    }

    // ✅ Get All Users (Admin / Manager only)
    @PostMapping("/all")
    public ResponseEntity<List<UserDTO>> getAllUsers(@RequestBody RequesterDTO request) {
        return ResponseEntity.ok(userService.getAllUsers(request.getRequesterId()));
    }

    // ✅ Get User by ID (Admin, Manager, or self)
    @PostMapping("/get-by-id/{id}")
    public ResponseEntity<UserDTO> getUserById(
            @PathVariable Long id,
            @RequestBody RequesterDTO request) {
        return ResponseEntity.ok(userService.getUserById(request.getRequesterId(), id));
    }

    // ✅ Get My Profile
    @PostMapping("/me")
    public ResponseEntity<UserDTO> getMyProfile(@RequestBody RequesterDTO request) {
        return ResponseEntity.ok(userService.getMyProfile(request.getRequesterId()));
    }

    // ✅ Create User
    @PostMapping
    public ResponseEntity<UserDTO> createUser(@Valid @RequestBody UserDTO dto) {
        UserDTO created = userService.createUser(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    // ✅ Update User (Self only)
    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> updateUser(
            @PathVariable Long id,
            @Valid @RequestBody UpdateUserRequest request) {
        return ResponseEntity.ok(userService.updateUser(request.getRequesterId(), id, request.getUser()));
    }

    // ✅ Delete User (Admin / Manager only)
    @PostMapping("/delete/{id}")
    public ResponseEntity<String> deleteUser(
            @PathVariable Long id,
            @RequestBody RequesterDTO request) {
        userService.deleteUser(request.getRequesterId(), id);
        return ResponseEntity.ok("User deleted successfully");
    }

    // ✅ Approve Manager (Admin only)
    @PostMapping("/approve-manager")
    public ResponseEntity<UserDTO> approveManager(@RequestBody UserActionRequest request) {
        return ResponseEntity.ok(
                userService.approveManager(request.getRequesterId(), request.getTargetUserId())
        );
    }

    // ✅ Login
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        LoginResponse response = userService.login(request);
        return ResponseEntity.ok(response);
    }
}
