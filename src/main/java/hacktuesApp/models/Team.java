package hacktuesApp.models;

import javax.persistence.*;
import java.util.*;

@Entity
@Table(name = "teams")
public class Team {
    private Integer id;

    private String name;

    private User leader;

    private Mentor mentor;

    private Set<User> users;

    private boolean wantTeammates;

    private String gitHubRepoUrl;

    private Set<Technology> technologies;

    private String otherTechnologies;

    private boolean organizer;

    private boolean finalist;

    private Integer place;

    private boolean winner;

    private String awards;

    public Team() { }

    public Team(String name, User leader, String gitHubRepoUrl) {
        this.name = name;
        this.leader = leader;
        this.users = new TreeSet<>(new Comparator<User>() {
            @Override
            public int compare(User user1, User user2) {
                String name1 = user1.getFullName().toLowerCase();
                String name2 = user2.getFullName().toLowerCase();
                return name1.compareTo(name2);
            }
        });
        this.wantTeammates = false;
        this.gitHubRepoUrl = gitHubRepoUrl;
        this.technologies = new TreeSet<>(new Comparator<Technology>() {
            @Override
            public int compare(Technology technology1, Technology technology2) {
                String name1 = technology1.getName().toLowerCase();
                String name2 = technology2.getName().toLowerCase();
                return name1.compareTo(name2);
            }
        });
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Column(name = "teamName", unique = true, nullable = false)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "leader_id")
    public User getLeader() {
        return leader;
    }

    public void setLeader(User leader) {
        this.leader = leader;
    }

    @ManyToOne
    @JoinColumn(name = "mentor_id")
    public Mentor getMentor() {
        return mentor;
    }

    public void setMentor(Mentor mentor) {
        this.mentor = mentor;
    }

    @OneToMany(mappedBy = "team")
    @OrderBy("fullName ASC")
    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }

    public void addUser(User user) {
        this.users.add(user);
    }

    public void deleteUser(User user) {
        this.users.remove(user);
    }

    @Column(name = "wantTeammates", nullable = false)
    public boolean getWantTeammates() {
        return wantTeammates;
    }

    public void setWantTeammates(boolean wantTeammates) {
        this.wantTeammates = wantTeammates;
    }

    @Column(name = "gitHubRepoUrl", nullable = false)
    public String getGitHubRepoUrl() {
        return gitHubRepoUrl;
    }

    public void setGitHubRepoUrl(String gitHubRepoUrl) {
        this.gitHubRepoUrl = gitHubRepoUrl;
    }

    @ManyToMany(fetch = FetchType.EAGER)
    @OrderBy("name ASC")
    @JoinTable(name = "teams_technologies")
    public Set<Technology> getTechnologies() {
        return technologies;
    }

    public void setTechnologies(Set<Technology> technologies) {
        this.technologies = technologies;
    }

    public void addTechnology(Technology technology) {
        this.technologies.add(technology);
    }

    @Column(name = "otherTechnologies", nullable = true)
    public String getOtherTechnologies() {
        return otherTechnologies;
    }

    public void setOtherTechnologies(String otherTechnologies) {
        this.otherTechnologies = otherTechnologies;
    }

    @Column(name = "organizer", nullable = false)
    public boolean isOrganizer() {
        return organizer;
    }

    public void setOrganizer(boolean organizer) {
        this.organizer = organizer;
    }

    @Column(name = "finalist", nullable = false)
    public boolean isFinalist() {
        return finalist;
    }

    public void setFinalist(boolean finalist) {
        this.finalist = finalist;
    }

    @Column(name = "place", nullable = false)
    public Integer getPlace() {
        return place;
    }

    public void setPlace(Integer place) {
        this.place = place;
    }

    @Column(name = "winner", nullable = false)
    public boolean isWinner() {
        return winner;
    }

    public void setWinner(boolean winner) {
        this.winner = winner;
    }

    @Column(name = "awards", nullable = true)
    public String getAwards() {
        return awards;
    }

    public void setAwards(String awards) {
        this.awards = awards;
    }
}
