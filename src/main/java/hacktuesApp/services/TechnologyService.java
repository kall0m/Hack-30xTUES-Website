package hacktuesApp.services;

import hacktuesApp.models.Technology;

import java.util.List;

public interface TechnologyService {
    Technology findTechnology(String name);
    List<Technology> getAllTechnologies();
    void saveTechnology(Technology technology);
}
