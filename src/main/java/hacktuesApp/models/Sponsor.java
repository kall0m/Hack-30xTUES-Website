package hacktuesApp.models;

import java.awt.*;

public class Sponsor {
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public Image getLogo() {
        return logo;
    }

    public void setLogo(Image logo) {
        this.logo = logo;
    }

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    public Sponsor() { }

    public Sponsor(Long id, String name, String website, Image logo, String rank) {
        this.id = id;
        this.name = name;
        this.website = website;
        this.logo = logo;
        this.rank = rank;
    }

    private Long id;
    private String name;
    private String website;
    private Image logo; //TODO
    private String rank;
}
