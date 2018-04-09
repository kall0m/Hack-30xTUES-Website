package hacktuesApp.services;

import hacktuesApp.models.Team;

import java.util.List;

public interface TeamService {
    List<Team> getAllTeams();

    Team findTeam(String name);

    Team findById(Integer id);

    boolean teamExists(Integer id);

    void deleteTeam(Team team);

    void saveTeam(Team team);
}
