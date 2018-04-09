package hacktuesApp.models;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;

@Entity
@Table(name = "mentors")
public class Mentor {
    private Integer id;

    private String email;

    private String fullName;

    private String information;

    private String hobbies;

    private String phone;

    private Set<Team> teams;

    private Integer teamCount;

    private String company;

    private String position;

    private String school;

    private String technologies;

    private String imageSource;

    private String attendance;

    public Mentor() { }

    public Mentor(String email, String fullName, String information, String hobbies, String phone,
                  String company, String position, String school, String technologies, String imageSource, String attendance) {
        this.email = email;
        this.fullName = fullName;
        this.information = information;
        this.hobbies = hobbies;
        this.phone = phone;
        this.teams = new TreeSet<>(new Comparator<Team>() {
            @Override
            public int compare(Team team1, Team team2) {
                String name1 = team1.getName().toLowerCase();
                String name2 = team2.getName().toLowerCase();
                return name1.compareTo(name2);
            }
        });
        this.company = company;
        this.position = position;
        this.school = school;
        this.technologies = technologies;
        this.imageSource = imageSource;
        this.attendance = attendance;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Column(name = "email", unique = true, nullable = false)
    @Email(message = "Please provide a valid e-mail")
    @NotEmpty(message = "Please provide an e-mail")
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Column(name = "fullName", nullable = false)
    @NotEmpty(message = "Please provide your full name")
    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    @Column(columnDefinition = "text")
    public String getInformation() {
        return information;
    }

    public void setInformation(String information) {
        this.information = information;
    }

    @Column(columnDefinition = "text")
    public String getHobbies() {
        return hobbies;
    }

    public void setHobbies(String hobbies) {
        this.hobbies = hobbies;
    }

    @Column(name = "phone")
    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @OneToMany(mappedBy = "mentor")
    @OrderBy("name ASC")
    public Set<Team> getTeams() {
        return teams;
    }

    public void setTeams(Set<Team> teams) {
        this.teams = teams;
    }

    public void addTeam(Team team) {
        this.teams.add(team);
    }

    public void deleteTeam(Team team) {
        this.teams.remove(team);
    }

    public Team getTeamAt(int n) {
        int index = 0;
        for(Team team : this.teams){
            if(index == n){
                return team;
            }
            index++;
        }

        return null;
    }

    @Column(name = "teamCount")
    public Integer getTeamCount() {
        return teamCount;
    }

    public void setTeamCount(Integer teamCount) {
        this.teamCount = teamCount;
    }

    @Column(name = "company")
    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    @Column(name = "position")
    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    @Column(name = "school")
    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    @Column(name = "technologies")
    public String getTechnologies() {
        return technologies;
    }

    public void setTechnologies(String technologies) {
        this.technologies = technologies;
    }

    @Column(name = "image_source")
    public String getImageSource() {
        return imageSource;
    }

    public void setImageSource(String imageSource) {
        this.imageSource = imageSource;
    }

    @Column(columnDefinition = "text")
    public String getAttendance() {
        return attendance;
    }

    public void setAttendance(String attendance) {
        this.attendance = attendance;
    }
}
