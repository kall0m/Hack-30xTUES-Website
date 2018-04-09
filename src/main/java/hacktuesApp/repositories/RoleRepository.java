package hacktuesApp.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import hacktuesApp.models.Role;

public interface RoleRepository extends JpaRepository<Role, Integer> {
    Role findByName(String name);
}