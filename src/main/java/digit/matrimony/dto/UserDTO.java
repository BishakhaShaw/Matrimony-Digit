//package digit.matrimony.dto;
//
//import jakarta.validation.constraints.Email;
//import jakarta.validation.constraints.NotBlank;
//import jakarta.validation.constraints.Size;
//import lombok.*;
//
//@Getter
//@Setter
//@NoArgsConstructor
//@AllArgsConstructor
//@Builder
//public class UserDTO {
//    private Long id;
//
//    @NotBlank
//    @Size(max = 50)
//    private String username;
//
//    @NotBlank
//    @Email
//    @Size(max = 100)
//    private String email;
//
//    private String permanentLocation;
//    private Short roleId;
//    private Long linkedUserId;
//    private String subscriptionType;
//    private Boolean isActive;
//
//    // For registration only
//    private String password;
//
//    // For responses when password is auto-generated
//    private String generatedPassword;
//}


























package digit.matrimony.dto;

import digit.matrimony.validation.ValidUserRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ValidUserRole   // ✅ Class-level validation
public class UserDTO {
    private Long id;

    @NotBlank(message = "Username is required")
    private String username;

    @Email(message = "Invalid email format")
    @NotBlank(message = "Email is required")
    private String email;

    private String permanentLocation;

    private String subscriptionType;

    private Boolean isActive;

    // Admin-only fields (optional in DTO for self-update)
    private Short roleId; // Users cannot update this themselves
    private String roleName;

    private Long linkedUserId; // Users cannot update this themselves
    private String linkedUsername;

    // Password handling
    private String password;            // Optional for self-update
    private String generatedPassword;   // Only returned when auto-generated
}
