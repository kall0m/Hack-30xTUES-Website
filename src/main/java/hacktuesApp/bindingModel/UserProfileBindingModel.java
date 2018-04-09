package hacktuesApp.bindingModel;

import javax.validation.constraints.NotNull;
import java.util.List;

public class UserProfileBindingModel {
    @NotNull
    private String fullName;

    @NotNull
    private String schoolClass;

    @NotNull
    private String diet;

    @NotNull
    private String tshirt;

    @NotNull
    private List<String> technologies;

    @NotNull
    private String otherTechnologies;

    @NotNull
    private Integer phone;

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getSchoolClass() {
        return schoolClass;
    }

    public void setSchoolClass(String schoolClass) {
        this.schoolClass = schoolClass;
    }

    public String getDiet() {
        return diet;
    }

    public void setDiet(String diet) {
        this.diet = diet;
    }

    public String getTshirt() {
        return tshirt;
    }

    public void setTshirt(String tshirt) {
        this.tshirt = tshirt;
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

    public Integer getPhone() {
        return phone;
    }

    public void setPhone(Integer phone) {
        this.phone = phone;
    }
}
