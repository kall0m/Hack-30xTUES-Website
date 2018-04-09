package hacktuesApp.services;

import hacktuesApp.models.Mentor;
import hacktuesApp.repositories.MentorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("mentorService")
public class MentorServiceStubImpl implements MentorService {
    private MentorRepository mentorRepository;

    @Autowired
    public MentorServiceStubImpl(MentorRepository mentorRepository) {
        this.mentorRepository = mentorRepository;
    }

    public Mentor findByEmail(String email) {
        return mentorRepository.findByEmail(email);
    }

    public boolean mentorExists(Integer id) {
        return mentorRepository.exists(id);
    }

    public Mentor findMentor(Integer id) {
        return mentorRepository.findOne(id);
    }

    public void saveMentor(Mentor mentor) {
        mentorRepository.saveAndFlush(mentor);
    }

    public List<Mentor> getAllMentors() {
        return mentorRepository.findAll();
    }
}
