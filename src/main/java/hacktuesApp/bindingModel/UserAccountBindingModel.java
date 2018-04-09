package hacktuesApp.bindingModel;

import javax.validation.constraints.NotNull;

public class UserAccountBindingModel {
    @NotNull
    private String oldPassword;

    @NotNull
    private String password;

    @NotNull
    private String confirmPassword;

    @NotNull
    private String email;

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
