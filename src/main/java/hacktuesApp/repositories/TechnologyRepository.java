package hacktuesApp.repositories;

import hacktuesApp.models.Technology;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TechnologyRepository extends JpaRepository<Technology, Integer> {
    Technology findByName(String name);
}
