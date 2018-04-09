package hacktuesApp.bindingModel;

import javax.validation.constraints.NotNull;
import java.util.List;

public class TeamSettingsBindingModel {
    @NotNull
    private String teamName;

    @NotNull
    private Boolean wantTeammates;

    @NotNull
    private String gitHubRepoUrl;

    @NotNull
    private List<String> technologies;

    @NotNull
    private String otherTechnologies;

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
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
}
