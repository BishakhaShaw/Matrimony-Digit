
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
    @Builder.Default
    private String subscriptionType = "N";   // default to N
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
