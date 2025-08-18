

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
