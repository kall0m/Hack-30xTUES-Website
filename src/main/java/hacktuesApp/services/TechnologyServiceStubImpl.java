package hacktuesApp.services;

import hacktuesApp.models.Technology;
import hacktuesApp.repositories.TechnologyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("technologyService")
public class TechnologyServiceStubImpl implements TechnologyService {
    private TechnologyRepository technologyRepository;

    @Autowired
    public TechnologyServiceStubImpl(TechnologyRepository technologyRepository) {
        this.technologyRepository = technologyRepository;
    }

    public Technology findTechnology(String name) {
        return technologyRepository.findByName(name);
    }

    public List<Technology> getAllTechnologies() {
        return technologyRepository.findAll();
    }

    public void saveTechnology(Technology technology) {
        technologyRepository.save(technology);
    }
}
