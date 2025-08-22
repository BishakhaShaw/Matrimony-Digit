//package digit.matrimony.security;
//
//import digit.matrimony.entity.User;
//import digit.matrimony.repository.UserRepository;
//import lombok.RequiredArgsConstructor;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.userdetails.*;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//
//@Service
//@RequiredArgsConstructor
//public class CustomUserDetailsService implements UserDetailsService {
//
//    private final UserRepository userRepository;
//
//    // We store role names like "Admin", "Manager", ...
//    // We will expose them to Spring Security as "ROLE_ADMIN", "ROLE_MANAGER", ...
//    private static String toSpringRole(User user) {
//        if (user.getRole() == null || user.getRole().getName() == null) return "ROLE_USER";
//        return "ROLE_" + user.getRole().getName().toUpperCase().replace(' ', '_');
//    }
//
//    @Override
//    public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {
//        User user = userRepository
//                .findByUsernameIgnoreCaseOrEmailIgnoreCase(usernameOrEmail, usernameOrEmail)
//                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
//
//        var authorities = List.of(new SimpleGrantedAuthority(toSpringRole(user)));
//
//        // We DO NOT use the hashed password here because auth is via JWT.
//        // But DaoAuthenticationProvider (if used) would expect the hash:
//        String passwordHash = user.getPasswordHash();
//
//        return org.springframework.security.core.userdetails.User
//                .withUsername(user.getUsername())
//                .password(passwordHash)   // required by provider even if we mostly do JWT
//                .authorities(authorities)
//                .accountLocked(Boolean.FALSE.equals(user.getIsActive()))
//                .disabled(Boolean.FALSE.equals(user.getIsActive()))
//                .build();
//    }
//}






















package digit.matrimony.security;

import digit.matrimony.entity.User;
import digit.matrimony.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {
        User user = userRepository.findByUsernameIgnoreCaseOrEmailIgnoreCase(usernameOrEmail, usernameOrEmail)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + usernameOrEmail));

        String roleName = user.getRole() != null ? "ROLE_" + user.getRole().getName().toUpperCase() : "ROLE_USER";

        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPasswordHash(),
                Collections.singletonList(new SimpleGrantedAuthority(roleName))
        );
    }
}
