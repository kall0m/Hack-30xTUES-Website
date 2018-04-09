package hacktuesApp.bindingModel;

import javax.validation.constraints.NotNull;

public class WantTeammatesBindingModel {
    @NotNull
    private Boolean wantTeammates;

    public Boolean getWantTeammates() {
        return wantTeammates;
    }

    public void setWantTeammates(Boolean wantTeammates) {
        this.wantTeammates = wantTeammates;
    }
}
