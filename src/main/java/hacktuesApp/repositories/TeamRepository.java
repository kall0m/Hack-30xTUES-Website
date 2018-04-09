package hacktuesApp.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import hacktuesApp.models.Team;

public interface TeamRepository extends JpaRepository<Team, Integer> {
    Team findByName(String name);
}