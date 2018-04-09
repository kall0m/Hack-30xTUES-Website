package hacktuesApp.bindingModel;

import javax.validation.constraints.NotNull;

public class UserEmailBindingModel {
    @NotNull
    private String email;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
