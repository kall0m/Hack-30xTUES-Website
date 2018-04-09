package hacktuesApp.models;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import java.util.*;

@Entity
@Table(name = "users")
public class User {
    private Integer id;

    private String email;

    private String fullName;

    private String password;

    private String schoolClass;

    private boolean enabled;

    private String confirmationToken;

    private String forgotPasswordToken;

    private Set<Role> roles;

    private Team team;

    private boolean leader;

    private Set<Post> posts;

    private String diet;

    private String tshirt;

    private Set<Technology> technologies;

    private String otherTechnologies;

    private String phone;

    public User() { }

    public User(String email, String fullName, String password, String schoolClass, String diet, String tshirt) {
        this.email = email;
        this.fullName = fullName;
        this.password = password;
        this.schoolClass = schoolClass;
        this.roles = new HashSet<>();
        this.team = null;
        this.leader = false;
        this.posts = new HashSet<>();
        this.diet = diet;
        this.tshirt = tshirt;
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

    @Column(name = "password", length = 60, nullable = false)
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Column(name = "schoolClass")
    public String getSchoolClass() {
        return schoolClass;
    }

    public void setSchoolClass(String schoolClass) {
        this.schoolClass = schoolClass;
    }

    @Column(name = "enabled")
    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @Column(name = "confirmationToken")
    public String getConfirmationToken() {
        return confirmationToken;
    }

    public void setConfirmationToken(String confirmationToken) {
        this.confirmationToken = confirmationToken;
    }

    @Column(name = "forgotPasswordToken")
    public String getForgotPasswordToken() {
        return forgotPasswordToken;
    }

    public void setForgotPasswordToken(String forgotPasswordToken) {
        this.forgotPasswordToken = forgotPasswordToken;
    }

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "users_roles")
    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public void addRole(Role role) {
        this.roles.add(role);
    }

    @ManyToOne()
    @JoinColumn(nullable = true, name = "teamId")
    @OneToOne(mappedBy = "leader")
    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    @OneToMany(mappedBy = "author")
    public Set<Post> getPosts() {
        return posts;
    }

    public void setPosts(Set<Post> posts) {
        this.posts = posts;
    }

    @Column(name = "leader", nullable = false)
    public boolean isLeader() {
        return leader;
    }

    public void setLeader(boolean leader) {
        this.leader = leader;
    }

    @Column(name = "diet", nullable = false)
    public String getDiet() {
        return diet;
    }

    public void setDiet(String diet) {
        this.diet = diet;
    }

    @Column(name = "tshirt", nullable = false)
    public String getTshirt() {
        return tshirt;
    }

    public void setTshirt(String tshirt) {
        this.tshirt = tshirt;
    }

    @ManyToMany(fetch = FetchType.EAGER)
    @OrderBy("name ASC")
    @JoinTable(name = "users_technologies")
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

    @Column(name = "phone", nullable = true)
    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}