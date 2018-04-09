package hacktuesApp.controllers;

import hacktuesApp.models.Mentor;
import hacktuesApp.models.User;
import hacktuesApp.services.MentorService;
import hacktuesApp.services.NotificationMessages;
import hacktuesApp.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Controller
public class MentorController {
    @Autowired
    private MentorService mentorService;
    @Autowired
    private UserService userService;

    @GetMapping("/mentors")
    public String mentors(Model model) {
        List<Mentor> mentors = this.mentorService.getAllMentors();

        Collections.sort(mentors, new Comparator<Mentor>(){
            public int compare(Mentor m1, Mentor m2){
                return m1.getFullName().compareTo(m2.getFullName());
            }
        });

        model.addAttribute("mentors", mentors);
        model.addAttribute("view", "info/mentors");
        //model.addAttribute("view", "maintenance/coming-soon");

        return "base-layout";
    }

    @GetMapping("/mentors/{id}")
    public String details(@PathVariable Integer id, Model model, RedirectAttributes redir) {
        if(!this.mentorService.mentorExists(id)) {
            redir.addFlashAttribute("message", NotificationMessages.MENTOR_DOESNT_EXIST);

            return "redirect:/mentors";
        }

        if(SecurityContextHolder.getContext().getAuthentication() != null
                && SecurityContextHolder.getContext().getAuthentication().isAuthenticated()
                && !(SecurityContextHolder.getContext().getAuthentication() instanceof AnonymousAuthenticationToken)) {

            UserDetails principal = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            User user = this.userService.findByEmail(principal.getUsername());

            model.addAttribute("user", user);
        }

        Mentor mentor = this.mentorService.findMentor(id);

        model.addAttribute("mentor", mentor);
        model.addAttribute("view", "info/mentor-details");

        return "base-layout";
    }
}
