package digit.matrimony.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PreferenceDTO {

    private Long id;

    private String preferredGender;


    private Long userId; // Reference to User entity by ID

    private Integer preferredAgeMin;

    private Integer preferredAgeMax;

    private String preferredReligion;

    private String preferredCaste;

    private String preferredLocation;

    private String preferredEducation;

    private String preferredMaritalStatus;
}
