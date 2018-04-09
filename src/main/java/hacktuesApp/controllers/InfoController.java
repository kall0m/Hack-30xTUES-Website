package hacktuesApp.controllers;

import hacktuesApp.models.Mentor;
import hacktuesApp.services.MentorService;
import hacktuesApp.services.NotificationMessages;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class InfoController {
    @GetMapping("/info")
    public String information(Model model) {
        model.addAttribute("view", "info/information");

        return "base-layout";
    }

    @GetMapping("/terms")
    public String terms(Model model) {
        model.addAttribute("view", "info/terms");

        return "base-layout";
    }

    @GetMapping("/program")
    public String program(Model model) {
        model.addAttribute("view", "info/program");

        return "base-layout";
    }

    @GetMapping("/topics")
    public String topics(Model model) {
        model.addAttribute("view", "info/topics");

        return "base-layout";
    }
}
