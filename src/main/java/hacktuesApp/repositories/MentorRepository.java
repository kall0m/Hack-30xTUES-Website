package hacktuesApp.repositories;

import hacktuesApp.models.Mentor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MentorRepository extends JpaRepository<Mentor, Integer> {
    Mentor findByEmail(String email);
}
