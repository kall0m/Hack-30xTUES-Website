package hacktuesApp.models;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "posts")
public class Post {
    private Integer id;

    private String title;

    private String content;

    private User author;

    private Date date = new Date();

    public Post() {
    }

    public Post(String title, String content, User author, Date date) {
        this.title = title;
        this.content = content;
        this.author = author;
        this.date = date;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Column(nullable = false)
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Column(columnDefinition = "text", nullable = false)
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @ManyToOne()
    @JoinColumn(nullable = false, name = "authorId")
    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    @Column(columnDefinition = "DATETIME", nullable = false)
    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Transient
    public String getSummary() {
        String summary = this.getContent().substring(0, 135);

        if(summary.charAt(summary.length() - 1) == ' ') {
            summary = summary.substring(0, 134);
        }

        summary = summary.concat("...");

        return summary;
    }
}