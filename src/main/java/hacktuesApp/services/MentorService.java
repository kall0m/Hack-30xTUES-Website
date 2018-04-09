package hacktuesApp.services;

import hacktuesApp.models.Mentor;

import java.util.List;

public interface MentorService {
    Mentor findByEmail(String email);

    boolean mentorExists(Integer id);

    Mentor findMentor(Integer id);

    void saveMentor(Mentor mentor);

    List<Mentor> getAllMentors();
}
