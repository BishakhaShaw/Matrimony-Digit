
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

    private Boolean isFamilyMember;


    private Long linkedUserId;


    private String linkedUsername;

    private Short roleId;
    private String generatedPassword;
}
