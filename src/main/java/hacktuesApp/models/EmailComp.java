package hacktuesApp.models;

import java.util.Comparator;

public class EmailComp implements Comparator<User> {
    @Override
    public int compare(User user1, User user2) {
        String email1 = user1.getEmail().toLowerCase();
        String email2 = user2.getEmail().toLowerCase();
        return email1.compareTo(email2);
    }
}
