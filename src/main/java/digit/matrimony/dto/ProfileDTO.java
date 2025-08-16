




package digit.matrimony.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProfileDTO {
    private Long id;
    private Long userId; // link to User
    private String gender;
    private LocalDateTime dateOfBirth;
    private String phone;
    private String aadhaarNumber;
    private String bio;
    private String religion;
    private String caste;
    private String education;
    private String occupation;
    private String income;
    private String currentLocation;
    private BigDecimal height;
    private String maritalStatus;
    private Boolean isVerified;
    private List<ProfilePhotoDTO> photos; // nested photos
}
