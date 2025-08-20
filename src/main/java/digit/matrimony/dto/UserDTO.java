//
//
//package digit.matrimony.dto;
//
//import digit.matrimony.validation.ValidUserRole;
//import jakarta.validation.constraints.Email;
//import jakarta.validation.constraints.NotBlank;
//import lombok.*;
//
//@Data
//@NoArgsConstructor
//@AllArgsConstructor
//@Builder
//@ValidUserRole   // ✅ Class-level validation
//public class UserDTO {
//    private Long id;
//
//    @NotBlank(message = "Username is required")
//    private String username;
//
//    @Email(message = "Invalid email format")
//    @NotBlank(message = "Email is required")
//    private String email;
//
//    private String permanentLocation;
//
//    private String subscriptionType;
//
//    private Boolean isActive;
//
//    // Admin-only fields (optional in DTO for self-update)
//    private Short roleId; // Users cannot update this themselves
//    private String roleName;
//
//    private Long linkedUserId; // Users cannot update this themselves
//    private String linkedUsername;
//
//    // Password handling
//    private String password;            // Optional for self-update
//    private String generatedPassword;   // Only returned when auto-generated
//}
















//
//
////MAIN CODE
//package digit.matrimony.dto;
//
//import lombok.*;
//
//@Getter
//@Setter
//@NoArgsConstructor
//@AllArgsConstructor
//@Builder
//public class UserDTO {
//
//    private Long id;
//    private String username;
//    private String email;
//    private String password;
//    private String permanentLocation;
//    private String subscriptionType;
//    private Boolean isActive;
//
//    private Long linkedUserId; // Users cannot update this themselves
//    private String linkedUsername;
//
//    private Short roleId;            // only role reference
//    private String generatedPassword; // used when system generates password
//}























package digit.matrimony.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDTO {

    private Long id;
    private String username;
    private String email;
    private String password;
    private String permanentLocation;
    private String subscriptionType;
    private Boolean isActive;

    // --- Family Member Flow ---
    /**
     * Marks whether this account is a family member's account.
     * If true -> linkedUserId must be provided.
     */
    private Boolean isFamilyMember;

    /**
     * References the parent (main) user account.
     * Mandatory when isFamilyMember = true.
     */
    private Long linkedUserId;

    /**
     * Parent (main) user’s username, populated only in responses
     * to help the frontend display "Family member of X".
     */
    private String linkedUsername;

    // --- Roles & System Generated Data ---
    private Short roleId;              // role reference only
    private String generatedPassword;  // set only when system generates password
}
