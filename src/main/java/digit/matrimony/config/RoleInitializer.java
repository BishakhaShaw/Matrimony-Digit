package digit.matrimony.config;

import digit.matrimony.entity.Role;
import digit.matrimony.repository.RoleRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class RoleInitializer implements CommandLineRunner {

    private final RoleRepository roleRepository;

    public RoleInitializer(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public void run(String... args) {
        List<Role> defaultRoles = Arrays.asList(
                new Role((short) 1, "Admin"),
                new Role((short) 2, "Manager"),
                new Role((short) 3, "User"),
                new Role((short) 4, "Family Member")
        );

        for (Role role : defaultRoles) {
            if (!roleRepository.existsById(role.getId())) {
                roleRepository.save(role);
            }
        }

        System.out.println("✅ Default roles inserted successfully!");
    }
}
