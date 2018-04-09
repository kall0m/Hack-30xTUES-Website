package hacktuesApp.controllers;

import hacktuesApp.models.Post;
import hacktuesApp.services.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Controller
public class BlogController {
    @Autowired
    private PostService postService;

    @GetMapping("/blog")
    public String posts(Model model) {
        List<Post> posts = this.postService.getAllPosts();

        Collections.sort(posts, new Comparator<Post>(){
            public int compare(Post p1, Post p2){
                return p2.getDate().compareTo(p1.getDate());
            }
        });

        model.addAttribute("view", "blog/posts");
        model.addAttribute("posts", posts);

        return "base-layout";
    }

    @GetMapping("/archive")
    public String archive(Model model) {
        //model.addAttribute("view", "blog/archive");
        model.addAttribute("view", "maintenance/coming-soon");

        return "base-layout";
    }
}
