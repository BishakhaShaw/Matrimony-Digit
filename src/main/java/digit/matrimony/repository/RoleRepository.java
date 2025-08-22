//package digit.matrimony.repository;
//
//import digit.matrimony.entity.Role;
//import org.springframework.data.jpa.repository.JpaRepository;
//
//import java.util.Optional;
//
//public interface RoleRepository extends JpaRepository<Role, Short> {
//
//    // ✅ Custom finder by role name
//    Optional<Role> findByName(String name);
//}






package digit.matrimony.repository;

import digit.matrimony.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Short> {

    Optional<Role> findByNameIgnoreCase(String name);
}
