//package digit.matrimony.dto;
//
//import lombok.Data;
//
//@Data
//public class LoginRequest {
//    private String emailOrUsername;  // Can accept either email or username
//    private String password;
//}






package digit.matrimony.dto;


import lombok.Data;


@Data
public class LoginRequest {
    private String emailOrUsername;
    private String password;
}