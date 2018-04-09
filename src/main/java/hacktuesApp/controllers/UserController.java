package hacktuesApp.controllers;

import com.nulabinc.zxcvbn.Strength;
import com.nulabinc.zxcvbn.Zxcvbn;
import hacktuesApp.bindingModel.*;
import hacktuesApp.models.Role;
import hacktuesApp.models.Technology;
import hacktuesApp.models.User;
import hacktuesApp.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

@Controller
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private EmailService emailService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private TechnologyService technologyService;

    @GetMapping("/register")
    public String register(Model model) {
        List<Technology> technologies = this.technologyService.getAllTechnologies();

        model.addAttribute("view", "user/register");
        model.addAttribute("technologies", technologies);

        return "base-layout";
    }

    @PostMapping("/register")
    public String registerProcess(UserBindingModel userBindingModel, BindingResult bindingResult, HttpServletRequest request, RedirectAttributes redir) {
        if (bindingResult.hasErrors()) {
            redir.addFlashAttribute("message", NotificationMessages.FILL_FORM_CORRECTLY);

            return "redirect:/register";
        }

        User userExists = this.userService.findByEmail(userBindingModel.getEmail());

        if (userExists != null) {
            redir.addFlashAttribute("message", NotificationMessages.USER_ALREADY_EXISTS);

            return "redirect:/register";
        }

        if(userBindingModel.getTechnologies() == null) {
            redir.addFlashAttribute("message", NotificationMessages.FILL_ALL_FIELDS);

            return "redirect:/register";
        }

        if(!userBindingModel.getPassword().equals(userBindingModel.getConfirmPassword())) {
            redir.addFlashAttribute("message", NotificationMessages.PASSWORDS_DONT_MATCH);

            return "redirect:/register";
        }

        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

        Zxcvbn passwordCheck = new Zxcvbn();

        Strength strength = passwordCheck.measure(userBindingModel.getPassword());

        if (strength.getScore() < 2) {
            redir.addFlashAttribute("message", NotificationMessages.PASSWORD_TOO_WEAK);

            return "redirect:/register";
        }

        if(!FormInformationService.schoolClasses.contains(userBindingModel.getSchoolClass())) {
            redir.addFlashAttribute("message", NotificationMessages.WRONG_SCHOOL_CLASS);

            return "redirect:/register";
        }

        if(!FormInformationService.diets.contains(userBindingModel.getDiet())) {
            redir.addFlashAttribute("message", NotificationMessages.WRONG_DIET_CHOICE);

            return "redirect:/register";
        }

        if(!FormInformationService.tshirts.contains(userBindingModel.getTshirt())) {
            redir.addFlashAttribute("message", NotificationMessages.WRONG_TSHIRT_SIZE);

            return "redirect:/register";
        }

        User user = new User(
                userBindingModel.getEmail(),
                userBindingModel.getFullName(),
                bCryptPasswordEncoder.encode(userBindingModel.getPassword()),
                userBindingModel.getSchoolClass(),
                userBindingModel.getDiet(),
                userBindingModel.getTshirt()
        );

        for(String t : userBindingModel.getTechnologies()) {
            Technology technology = this.technologyService.findTechnology(t);

            if(technology == null) {
                redir.addFlashAttribute("message", NotificationMessages.OTHER_TECHNOLOGIES);
                return "redirect:/register";
            }

            user.addTechnology(technology);
        }

        if(!userBindingModel.getOtherTechnologies().isEmpty()) {
            /*for (String t : userBindingModel.getOtherTechnologies().trim().split("\\s*,\\s*")) {
                Technology technology = this.technologyService.findTechnology(t);

                if(technology == null) {
                    technology = new Technology();
                    technology.setName(t);

                    user.addTechnology(technology);
                    this.technologyService.saveTechnology(technology);
                }
            }*/
            user.setOtherTechnologies(userBindingModel.getOtherTechnologies());
        }

        Role userRole = this.roleService.findRole("ROLE_USER");

        user.addRole(userRole);

        // Disable user until they click on confirmation link in email
        user.setEnabled(false);

        // Generate random 36-character string token for confirmation link
        user.setConfirmationToken(UUID.randomUUID().toString());

        this.userService.saveUser(user);

        String appUrl = request.getScheme() + "://" + request.getServerName();

        SimpleMailMessage registrationEmail = this.emailService.createEmail(
                user.getEmail(),
                EmailDrafts.USER_REGISTRATION_SUBJECT,
                EmailDrafts.USER_REGISTRATION_CONTENT(appUrl, user.getConfirmationToken()),
                "hacktues@elsys-bg.org"
        );

        emailService.sendEmail(registrationEmail);

        redir.addFlashAttribute("message", NotificationMessages.USER_CONFIRMATION_EMAIL_SENT(user.getEmail()));

        return "redirect:/";
    }

    @GetMapping("/confirm")
    public String confirm(@RequestParam("token") String token, RedirectAttributes redir) {
        User user = userService.findByConfirmationToken(token);

        if (user == null) { // No token found in DB
            redir.addFlashAttribute("message", NotificationMessages.WRONG_CONFIRMATION_LINK);
            return "redirect:/";
        } else { // Token found
            user.setEnabled(true);
            this.userService.saveUser(user);
        }

        redir.addFlashAttribute("message", NotificationMessages.USER_EMAIL_SUCCESSFULLY_CONFIRMED);

        return "redirect:/login";
    }

    @GetMapping("/login")
    public String login(@RequestParam(value = "error", required = false) String error, @RequestParam(value = "logout", required = false) String logout, Model model) {
        if(error != null) {
            model.addAttribute("message", NotificationMessages.WRONG_EMAIL_OR_PASSWORD);
        }

        if(logout != null) {
            model.addAttribute("message", NotificationMessages.USER_SUCCESSFULLY_LOGGED_OUT);
        }

        model.addAttribute("view", "user/login");

        return "base-layout";
    }

    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public String logoutPage(HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if(auth != null) {
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }

        return "redirect:/login?logout";
    }

    @GetMapping("/profile")
    @PreAuthorize("isAuthenticated()")
    public String profilePage(Model model) {
        UserDetails principal = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = this.userService.findByEmail(principal.getUsername());

        List<Technology> technologies = new ArrayList<>(user.getTechnologies());

        model.addAttribute("user", user);
        model.addAttribute("technologies", technologies);
        model.addAttribute("view", "user/profile");

        return "base-layout";
    }

    /*@GetMapping("/profile/settings")
    @PreAuthorize("isAuthenticated()")
    public String profileSettings(Model model) {
        UserDetails principal = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = this.userService.findByEmail(principal.getUsername());

        List<Technology> technologies = this.technologyService.getAllTechnologies();

        model.addAttribute("view", "user/settings");
        model.addAttribute("user", user);
        model.addAttribute("technologies", technologies);

        return "base-layout";
    }

    @PostMapping("/profile/settings")
    @PreAuthorize("isAuthenticated()")
    public String profileSettingsProcess(UserProfileBindingModel userProfileBindingModel, BindingResult bindingResult, RedirectAttributes redir) {
        if (bindingResult.hasErrors()) {
            redir.addFlashAttribute("message", NotificationMessages.FILL_FORM_CORRECTLY);

            return "redirect:/profile/settings";
        }

        if(userProfileBindingModel.getTechnologies() == null) {
            redir.addFlashAttribute("message", NotificationMessages.FILL_ALL_FIELDS);

            return "redirect:/profile/settings";
        }

        UserDetails principal = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = this.userService.findByEmail(principal.getUsername());

        if(userProfileBindingModel.getFullName().equals(user.getFullName()) && //TODO
                userProfileBindingModel.getSchoolClass().equals(user.getSchoolClass()) &&
                userProfileBindingModel.getDiet().equals(user.getDiet()) &&
                userProfileBindingModel.getTshirt().equals(user.getTshirt()) &&
                userProfileBindingModel.getOtherTechnologies().equals(user.getOtherTechnologies())) {

            List<String> technologiesNames = new ArrayList<>();

            for(Technology t : user.getTechnologies()) {
                technologiesNames.add(t.getName());
            }

            if(userProfileBindingModel.getTechnologies().equals(technologiesNames) && userProfileBindingModel.getOtherTechnologies().isEmpty()) {
                return "redirect:/profile";
            }
        }

        if(!FormInformationService.schoolClasses.contains(userProfileBindingModel.getSchoolClass())) {
            redir.addFlashAttribute("message", NotificationMessages.WRONG_SCHOOL_CLASS);

            return "redirect:/profile/settings";
        }

        if(!FormInformationService.diets.contains(userProfileBindingModel.getDiet())) {
            redir.addFlashAttribute("message", NotificationMessages.WRONG_DIET_CHOICE);

            return "redirect:/profile/settings";
        }

        if(!FormInformationService.tshirts.contains(userProfileBindingModel.getTshirt())) {
            redir.addFlashAttribute("message", NotificationMessages.WRONG_TSHIRT_SIZE);

            return "redirect:/profile/settings";
        }

        user.setFullName(userProfileBindingModel.getFullName());
        user.setSchoolClass(userProfileBindingModel.getSchoolClass());
        user.setDiet(userProfileBindingModel.getDiet());
        user.setTshirt(userProfileBindingModel.getTshirt());

        user.setTechnologies(new TreeSet<>(new Comparator<Technology>() {
            @Override
            public int compare(Technology technology1, Technology technology2) {
                String name1 = technology1.getName().toLowerCase();
                String name2 = technology2.getName().toLowerCase();
                return name1.compareTo(name2);
            }
        }));

        for(String t : userProfileBindingModel.getTechnologies()) {
            Technology technology = this.technologyService.findTechnology(t);

            if(technology == null) {
                redir.addFlashAttribute("message", NotificationMessages.OTHER_TECHNOLOGIES);
                return "redirect:/profile/settings";
            }

            user.addTechnology(technology);
        }

        user.setOtherTechnologies(null);

        if(!userProfileBindingModel.getOtherTechnologies().isEmpty()) {
            /*for (String t : userProfileBindingModel.getOtherTechnologies().trim().split("\\s*,\\s*")) {
                Technology technology = this.technologyService.findTechnology(t);

                if(technology == null) {
                    technology = new Technology();
                    technology.setName(t);

                    user.addTechnology(technology);
                    this.technologyService.saveTechnology(technology);
                }
            }

            user.setOtherTechnologies(userProfileBindingModel.getOtherTechnologies());
        }

        this.userService.saveUser(user);

        redir.addFlashAttribute("message", NotificationMessages.CHANGES_SAVED);

        return "redirect:/profile";
    }

    @GetMapping("/profile/admin/settings")
    @PreAuthorize("isAuthenticated()")
    public String profileAdminSettings(Model model) {
        UserDetails principal = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = this.userService.findByEmail(principal.getUsername());

        model.addAttribute("user", user);
        model.addAttribute("view", "user/admin-settings");

        return "base-layout";
    }

    @PostMapping("/profile/admin/settings")
    @PreAuthorize("isAuthenticated()")
    public String profileAdminSettingsProcess(UserAccountBindingModel userAccountBindingModel, BindingResult bindingResult, RedirectAttributes redir) {
        if (bindingResult.hasErrors()) {
            redir.addFlashAttribute("message", NotificationMessages.FILL_FORM_CORRECTLY);

            return "redirect:/admin/settings";
        }

        UserDetails principal = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = this.userService.findByEmail(principal.getUsername());

        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

        if(userAccountBindingModel.getEmail().equals(user.getEmail()) &&
                bCryptPasswordEncoder.matches(userAccountBindingModel.getPassword(), user.getPassword())) {
            return "redirect:/profile";
        }

        if(!(userAccountBindingModel.getEmail().equals(user.getEmail())) && this.userService.findByEmail(userAccountBindingModel.getEmail()) != null) {
            redir.addFlashAttribute("message", NotificationMessages.USER_ALREADY_EXISTS);

            return "redirect:/profile/admin/settings";
        }

        if(!bCryptPasswordEncoder.matches(userAccountBindingModel.getOldPassword(), user.getPassword())) {
            redir.addFlashAttribute("message", NotificationMessages.PASSWORDS_DONT_MATCH);

            return "redirect:/profile/admin/settings";
        }

        if(!userAccountBindingModel.getPassword().equals(userAccountBindingModel.getConfirmPassword())) {
            redir.addFlashAttribute("message", NotificationMessages.PASSWORDS_DONT_MATCH);

            return "redirect:/profile/admin/settings";
        }

        Zxcvbn passwordCheck = new Zxcvbn();

        Strength strength = passwordCheck.measure(userAccountBindingModel.getPassword());

        if (strength.getScore() < 3) {
            redir.addFlashAttribute("message", NotificationMessages.PASSWORD_TOO_WEAK);

            return "redirect:/register";
        }

        user.setEmail(userAccountBindingModel.getEmail());
        user.setPassword(bCryptPasswordEncoder.encode(userAccountBindingModel.getPassword()));

        this.userService.saveUser(user);

        Authentication authentication = new PreAuthenticatedAuthenticationToken(user, user.getPassword());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        redir.addFlashAttribute("message", NotificationMessages.CHANGES_SAVED);

        return "redirect:/profile";
    }*/

    @GetMapping("/user/forgot_password")
    public String forgotPassword(Model model) {
        model.addAttribute("view", "user/forgot-password");

        return "base-layout";
    }

    @PostMapping("/user/forgot_password")
    public String forgotPasswordProcess(ForgotPasswordBindingModel forgotPasswordBindingModel, BindingResult bindingResult, RedirectAttributes redir, HttpServletRequest request) {
        if (bindingResult.hasErrors()) {
            redir.addFlashAttribute("message", NotificationMessages.FILL_FORM_CORRECTLY);

            return "redirect:/user/forgot_password";
        }

        User user = this.userService.findByEmail(forgotPasswordBindingModel.getEmail());

        if (user == null) {
            redir.addFlashAttribute("message", NotificationMessages.USER_DOESNT_EXISTS(forgotPasswordBindingModel.getEmail()));

            return "redirect:/register";
        }

        // Generate random 36-character string token for forgot password link
        user.setForgotPasswordToken(UUID.randomUUID().toString());

        this.userService.saveUser(user);

        String appUrl = request.getScheme() + "://" + request.getServerName();

        SimpleMailMessage forgotPasswordEmail = this.emailService.createEmail(
                user.getEmail(),
                EmailDrafts.USER_FORGOT_PASSWORD_SUBJECT,
                EmailDrafts.USER_FORGOT_PASSWORD_CONTENT(appUrl, user.getForgotPasswordToken()),
                "hacktues@elsys-bg.org"
        );

        emailService.sendEmail(forgotPasswordEmail);

        redir.addFlashAttribute("message", NotificationMessages.USER_FORGOT_PASSWORD_CONFIRMATION_EMAIL_SENT(user.getEmail()));

        return "redirect:/";
    }

    @GetMapping("/user/set_new_password")
    public String setNewPassword(@RequestParam("token") String token, Model model, RedirectAttributes redir) {
        User user = this.userService.findByForgotPasswordToken(token);

        if(user == null) {
            redir.addFlashAttribute("message", NotificationMessages.USER_DOESNT_HAVE_FORGOT_PASSWORD_CONFIRMATION_TOKEN);

            return "redirect:/user/forgot_password";
        }

        model.addAttribute("user", user);
        model.addAttribute("view", "user/set-new-password");

        return "base-layout";
    }

    @PostMapping("/user/set_new_password")
    public String setNewPasswordProcess(@RequestParam("token") String token, SetNewPasswordBindingModel setNewPasswordBindingModel, BindingResult bindingResult, RedirectAttributes redir) {
        if (bindingResult.hasErrors()) {
            redir.addFlashAttribute("message", NotificationMessages.FILL_FORM_CORRECTLY);

            return "redirect:/user/set_new_password";
        }

        User user = userService.findByForgotPasswordToken(token);

        if (user == null) { // No token found in DB
            redir.addFlashAttribute("message", NotificationMessages.USER_DOESNT_HAVE_FORGOT_PASSWORD_CONFIRMATION_TOKEN);

            return "redirect:/user/forgot_password";
        } else { // Token found
            if(!setNewPasswordBindingModel.getNewPassword().equals(setNewPasswordBindingModel.getConfirmPassword())) {
                redir.addFlashAttribute("message", NotificationMessages.PASSWORDS_DONT_MATCH);

                return "redirect:/user/set_new_password?token=" + token;
            }

            BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

            Zxcvbn passwordCheck = new Zxcvbn();

            Strength strength = passwordCheck.measure(setNewPasswordBindingModel.getNewPassword());

            if (strength.getScore() < 2) {
                redir.addFlashAttribute("message", NotificationMessages.PASSWORD_TOO_WEAK);

                return "redirect:/user/set_new_password?token=" + token;
            }

            // Set the reset token to null so it cannot be used again
            user.setForgotPasswordToken(null);

            user.setPassword(bCryptPasswordEncoder.encode(setNewPasswordBindingModel.getNewPassword()));

            this.userService.saveUser(user);
        }

        redir.addFlashAttribute("message", NotificationMessages.CHANGES_SAVED);

        return "redirect:/login";
    }
}