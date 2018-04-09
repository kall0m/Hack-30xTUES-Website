package hacktuesApp.repositories;

import hacktuesApp.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {
    User findByEmail(String email);
    User findByConfirmationToken(String confirmationToken);
    User findByForgotPasswordToken(String forgotPasswordToken);
}
