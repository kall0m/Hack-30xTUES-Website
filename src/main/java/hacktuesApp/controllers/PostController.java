package hacktuesApp.controllers;

import hacktuesApp.bindingModel.PostBindingModel;
import hacktuesApp.models.Post;
import hacktuesApp.models.Role;
import hacktuesApp.models.User;
import hacktuesApp.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Controller
public class PostController {
    @Autowired
    private UserService userService;
    @Autowired
    private PostService postService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private EmailService emailService;

    @GetMapping("/blog/post/create")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String create(Model model, RedirectAttributes redir) {
        UserDetails principal = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = this.userService.findByEmail(principal.getUsername());

        Role adminRole = this.roleService.findRole("ROLE_ADMIN");

        if(!user.getRoles().contains(adminRole)) {
            redir.addFlashAttribute("message", NotificationMessages.ACCESS_DENIED);

            return "redirect:/blog";
        }

        model.addAttribute("view", "post/create");

        return "base-layout";
    }

    @PostMapping("/blog/post/create")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String createProcess(PostBindingModel postBindingModel, BindingResult bindingResult, RedirectAttributes redir, HttpServletRequest request) {
        if (bindingResult.hasErrors()) {
            redir.addFlashAttribute("message", NotificationMessages.FILL_FORM_CORRECTLY);

            return "redirect:/blog/post/create";
        }

        UserDetails principal = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = this.userService.findByEmail(principal.getUsername());

        Role adminRole = this.roleService.findRole("ROLE_ADMIN");

        if(!user.getRoles().contains(adminRole)) {
            redir.addFlashAttribute("message", NotificationMessages.ACCESS_DENIED);

            return "redirect:/blog";
        }

        Post post = new Post(
                postBindingModel.getTitle(),
                postBindingModel.getContent(),
                user,
                new Date()
        );

        this.postService.savePost(post);

        String appUrl = request.getScheme() + "://" + request.getServerName();

        SimpleMailMessage newPostEmail;

        List<User> users = this.userService.getAllUsers();

        for (User u: users) {
            newPostEmail = this.emailService.createEmail(
                    u.getEmail(),
                    EmailDrafts.NEW_POST_IN_BLOG_SUBJECT,
                    EmailDrafts.NEW_POST_IN_BLOG_CONTENT(user.getFullName(), appUrl, post.getId()),
                    "hacktues@elsys-bg.org"
            );

            emailService.sendEmail(newPostEmail);
        }

        redir.addFlashAttribute("message", NotificationMessages.BLOG_POST_SUCCESSFULLY_CREATED);

        return "redirect:/blog";
    }

    @GetMapping("/blog/post/{id}")
    public String details(@PathVariable Integer id, Model model, RedirectAttributes redir) {
        if(!this.postService.postExists(id)) {
            redir.addFlashAttribute("message", NotificationMessages.BLOG_POST_DOESNT_EXIST);

            return "redirect:/blog";
        }

        Post post = this.postService.findPost(id);

        if(SecurityContextHolder.getContext().getAuthentication() != null
            && SecurityContextHolder.getContext().getAuthentication().isAuthenticated()
            && !(SecurityContextHolder.getContext().getAuthentication() instanceof AnonymousAuthenticationToken)) {

            UserDetails principal = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            User user = this.userService.findByEmail(principal.getUsername());

            model.addAttribute("user", user);
        }

        DateFormat df = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
        String date = df.format(post.getDate());

        model.addAttribute("date", date);
        model.addAttribute("post", post);
        model.addAttribute("view", "post/details");

        return "base-layout";
    }

    @GetMapping("/blog/post/edit/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String edit(@PathVariable Integer id, Model model, RedirectAttributes redir) {
        UserDetails principal = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = this.userService.findByEmail(principal.getUsername());

        Role adminRole = this.roleService.findRole("ROLE_ADMIN");

        if(!user.getRoles().contains(adminRole)) {
            redir.addFlashAttribute("message", NotificationMessages.ACCESS_DENIED);

            return "redirect:/blog";
        }

        if(!this.postService.postExists(id)) {
            redir.addFlashAttribute("message", NotificationMessages.BLOG_POST_DOESNT_EXIST);

            return "redirect:/blog";
        }

        Post post = this.postService.findPost(id);

        model.addAttribute("post", post);
        model.addAttribute("view", "post/edit");

        return "base-layout";
    }

    @PostMapping("/blog/post/edit/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String editProcess(@PathVariable Integer id, PostBindingModel postBindingModel, BindingResult bindingResult, RedirectAttributes redir) {
        if (bindingResult.hasErrors()) {
            redir.addFlashAttribute("message", NotificationMessages.FILL_FORM_CORRECTLY);

            return "redirect:/blog/post/edit/" + id;
        }

        UserDetails principal = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = this.userService.findByEmail(principal.getUsername());

        Role adminRole = this.roleService.findRole("ROLE_ADMIN");

        if(!user.getRoles().contains(adminRole)) {
            redir.addFlashAttribute("message", NotificationMessages.ACCESS_DENIED);

            return "redirect:/blog";
        }

        if(!this.postService.postExists(id)) {
            redir.addFlashAttribute("message", NotificationMessages.BLOG_POST_DOESNT_EXIST);

            return "redirect:/blog";
        }

        Post post = this.postService.findPost(id);

        post.setTitle(postBindingModel.getTitle());
        post.setContent(postBindingModel.getContent());

        this.postService.savePost(post);

        redir.addFlashAttribute("message", NotificationMessages.CHANGES_SAVED);

        return "redirect:/blog/post/" + post.getId();
    }

    @GetMapping("/blog/post/delete/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String delete(@PathVariable Integer id, Model model, RedirectAttributes redir) {
        UserDetails principal = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = this.userService.findByEmail(principal.getUsername());

        Role adminRole = this.roleService.findRole("ROLE_ADMIN");

        if(!user.getRoles().contains(adminRole)) {
            redir.addFlashAttribute("message", NotificationMessages.ACCESS_DENIED);

            return "redirect:/blog";
        }

        if(!this.postService.postExists(id)) {
            redir.addFlashAttribute("message", NotificationMessages.BLOG_POST_DOESNT_EXIST);

            return "redirect:/blog";
        }

        Post post = this.postService.findPost(id);

        model.addAttribute("post", post);
        model.addAttribute("view", "post/delete");

        return "base-layout";
    }

    @PostMapping("/blog/post/delete/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String deleteProcess(@PathVariable Integer id, RedirectAttributes redir) {
        UserDetails principal = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = this.userService.findByEmail(principal.getUsername());

        Role adminRole = this.roleService.findRole("ROLE_ADMIN");

        if(!user.getRoles().contains(adminRole)) {
            redir.addFlashAttribute("message", NotificationMessages.ACCESS_DENIED);

            return "redirect:/blog";
        }

        if(!this.postService.postExists(id)) {
            redir.addFlashAttribute("message", NotificationMessages.BLOG_POST_DOESNT_EXIST);

            return "redirect:/blog";
        }

        Post post = this.postService.findPost(id);

        this.postService.deletePost(post);

        redir.addFlashAttribute("message", NotificationMessages.CHANGES_SAVED);

        return "redirect:/blog";
    }
}
