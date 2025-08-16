
package digit.matrimony.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProfilePhotoDTO {
    private Long id;
    private Long profileId; // link to Profile
    private String photoUrl;
    private Boolean isProfilePhoto;
    private LocalDateTime uploadedAt;
}
