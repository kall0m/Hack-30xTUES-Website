package hacktuesApp.services;

import hacktuesApp.models.User;
import org.springframework.stereotype.Service;

import java.util.List;

public interface UserService {
    boolean authenticate(String username, String password);

    User findByEmail(String email);

    User findByConfirmationToken(String confirmationToken);

    User findByForgotPasswordToken(String confirmationToken);

    List<User> getAllUsers();

    void saveUser(User user);
}
