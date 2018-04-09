package hacktuesApp.services;

import hacktuesApp.models.Team;
import hacktuesApp.repositories.TeamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("teamService")
public class TeamServiceStubImpl implements TeamService {
    private TeamRepository teamRepository;

    @Autowired
    public TeamServiceStubImpl(TeamRepository teamRepository) {
        this.teamRepository = teamRepository;
    }

    public List<Team> getAllTeams() {
        return teamRepository.findAll();
    }

    public Team findTeam(String name) {
        return teamRepository.findByName(name);
    }

    public Team findById(Integer id) {
        return teamRepository.findOne(id);
    }

    public boolean teamExists(Integer id) {
        return teamRepository.exists(id);
    }

    public void deleteTeam(Team team) {
        teamRepository.delete(team);
    }

    public void saveTeam(Team team) {
        teamRepository.saveAndFlush(team);
    }
}
