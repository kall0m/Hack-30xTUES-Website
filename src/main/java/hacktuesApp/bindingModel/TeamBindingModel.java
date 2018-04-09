package hacktuesApp.bindingModel;

import javax.validation.constraints.NotNull;
import java.util.List;

public class TeamBindingModel {
    @NotNull
    private String teamName;

    @NotNull
    private Integer participantsCount;

    @NotNull
    private String email1;

    @NotNull
    private String email2;

    @NotNull
    private String email3;

    private String email4;

    private String email5;

    @NotNull
    private Boolean wantTeammates;

    @NotNull
    private String gitHubRepoUrl;

    @NotNull
    private List<String> technologies;

    @NotNull
    private String otherTechnologies;

    @NotNull
    private String phone;

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public Integer getParticipantsCount() {
        return participantsCount;
    }

    public void setParticipantsCount(Integer participantsCount) {
        this.participantsCount = participantsCount;
    }

    public String getEmail1() {
        return email1;
    }

    public void setEmail1(String email1) {
        this.email1 = email1;
    }

    public String getEmail2() {
        return email2;
    }

    public void setEmail2(String email2) {
        this.email2 = email2;
    }

    public String getEmail3() {
        return email3;
    }

    public void setEmail3(String email3) {
        this.email3 = email3;
    }

    public String getEmail4() {
        return email4;
    }

    public void setEmail4(String email4) {
        this.email4 = email4;
    }

    public String getEmail5() {
        return email5;
    }

    public void setEmail5(String email5) {
        this.email5 = email5;
    }

    public Boolean getWantTeammates() {
        return wantTeammates;
    }

    public void setWantTeammates(Boolean wantTeammates) {
        this.wantTeammates = wantTeammates;
    }

    public String getGitHubRepoUrl() {
        return gitHubRepoUrl;
    }

    public void setGitHubRepoUrl(String gitHubRepoUrl) {
        this.gitHubRepoUrl = gitHubRepoUrl;
    }

    public List<String> getTechnologies() {
        return technologies;
    }

    public void setTechnologies(List<String> technologies) {
        this.technologies = technologies;
    }

    public String getOtherTechnologies() {
        return otherTechnologies;
    }

    public void setOtherTechnologies(String otherTechnologies) {
        this.otherTechnologies = otherTechnologies;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
