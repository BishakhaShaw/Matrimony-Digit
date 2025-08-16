//package digit.matrimony.dto;
//
//import lombok.Builder;
//import lombok.Getter;
//import lombok.Setter;
//import lombok.NoArgsConstructor;
//import lombok.AllArgsConstructor;
//
//import java.time.LocalDateTime;
//
//@Getter
//@Setter
//@NoArgsConstructor
//@AllArgsConstructor
//@Builder
//public class ProfilePhotoDTO {
//    private Long id;
//    private Long profileId;
//    private String photoUrl;
//    private Boolean isProfilePhoto;
//    private LocalDateTime uploadedAt;
//}











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
