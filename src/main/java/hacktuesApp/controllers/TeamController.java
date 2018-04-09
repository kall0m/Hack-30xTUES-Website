package hacktuesApp.controllers;

import hacktuesApp.bindingModel.TeamSettingsBindingModel;
import hacktuesApp.bindingModel.UserEmailBindingModel;
import hacktuesApp.bindingModel.TeamBindingModel;
import hacktuesApp.models.Mentor;
import hacktuesApp.models.Team;
import hacktuesApp.models.Technology;
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
import java.util.*;
import java.util.stream.Collectors;

@Controller
public class TeamController {
    @Autowired
    private TeamService teamService;
    @Autowired
    private UserService userService;
    @Autowired
    private EmailService emailService;
    @Autowired
    private TechnologyService technologyService;
    @Autowired
    private MentorService mentorService;

    /*@GetMapping("/team/limit/reached")
    @PreAuthorize("isAuthenticated()")
    public String limitReched(Model model) {
        model.addAttribute("view", "team/teams-limit-reached");

        return "base-layout";
    }*/

    /*@GetMapping("/team/create")
    @PreAuthorize("isAuthenticated()")
    public String create(Model model, RedirectAttributes redir) {
        if(this.teamService.getAllTeams().size() >= 47) {
            return "redirect:/team/limit/reached";
        }

        UserDetails principal = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = this.userService.findByEmail(principal.getUsername());

        if(user.getTeam() != null) {
            redir.addFlashAttribute("message", NotificationMessages.USER_HAS_TEAM);

            return "redirect:/team";
        }

        List<Technology> technologies = this.technologyService.getAllTechnologies();

        model.addAttribute("technologies", technologies);
        model.addAttribute("leader", user);
        model.addAttribute("view", "team/create");

        return "base-layout";
    }

    @PostMapping("/team/create")
    @PreAuthorize("isAuthenticated()")
    public String createProcess(TeamBindingModel teamBindingModel, BindingResult bindingResult, RedirectAttributes redir, HttpServletRequest request) {
        if(this.teamService.getAllTeams().size() >= 47) {
            return "redirect:/team/limit/reached";
        }

        if (bindingResult.hasErrors()) {
            redir.addFlashAttribute("message", NotificationMessages.FILL_FORM_CORRECTLY);

            return "redirect:/team/create";
        }

        if(teamBindingModel.getTechnologies() == null) {
            redir.addFlashAttribute("message", NotificationMessages.FILL_ALL_FIELDS);

            return "redirect:/team/create";
        }

        if(teamBindingModel.getParticipantsCount() < 3 || teamBindingModel.getParticipantsCount() > 5) {
            redir.addFlashAttribute("message", NotificationMessages.MINIMAL_AND_MAXIMAL_PARTICIPANTS_COUNT);

            return "redirect:/team/create";
        }

        UserDetails principal = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = this.userService.findByEmail(principal.getUsername());

        if(user.getTeam() != null) {
            redir.addFlashAttribute("message", NotificationMessages.USER_HAS_TEAM);

            return "redirect:/team";
        }

        if(this.teamService.findTeam(teamBindingModel.getTeamName()) != null) {
            redir.addFlashAttribute("message", NotificationMessages.TEAM_ALREADY_EXISTS);

            return "redirect:/team/create";
        }

        if(teamBindingModel.getEmail1().equals(teamBindingModel.getEmail2()) ||
                teamBindingModel.getEmail1().equals(teamBindingModel.getEmail3()) ||
                teamBindingModel.getEmail1().equals(teamBindingModel.getEmail4()) ||
                teamBindingModel.getEmail1().equals(teamBindingModel.getEmail5()) ||
                teamBindingModel.getEmail2().equals(teamBindingModel.getEmail3()) ||
                teamBindingModel.getEmail2().equals(teamBindingModel.getEmail4()) ||
                teamBindingModel.getEmail2().equals(teamBindingModel.getEmail5()) ||
                teamBindingModel.getEmail3().equals(teamBindingModel.getEmail4()) ||
                teamBindingModel.getEmail3().equals(teamBindingModel.getEmail5()) ||
                teamBindingModel.getEmail4().equals(teamBindingModel.getEmail5()) &&
                !teamBindingModel.getEmail1().isEmpty() &&
                !teamBindingModel.getEmail2().isEmpty() &&
                !teamBindingModel.getEmail3().isEmpty() &&
                !teamBindingModel.getEmail4().isEmpty() &&
                !teamBindingModel.getEmail5().isEmpty()) {
            redir.addFlashAttribute("message", NotificationMessages.CANT_USE_EMAIL_MULTIPLE_TIMES);

            return "redirect:/team/create";
        }

        Team team = new Team(
                teamBindingModel.getTeamName(),
                user,
                teamBindingModel.getGitHubRepoUrl()
        );

        for(String t : teamBindingModel.getTechnologies()) {
            Technology technology = this.technologyService.findTechnology(t);

            if(technology == null) {
                redir.addFlashAttribute("message", NotificationMessages.OTHER_TECHNOLOGIES);
                return "redirect:/team/create";
            }

            team.addTechnology(technology);
        }

        if(!teamBindingModel.getOtherTechnologies().isEmpty()) {
            /*for (String t : teamBindingModel.getOtherTechnologies().trim().split("\\s*,\\s*")) {
                Technology technology = this.technologyService.findTechnology(t);

                if(technology == null) {
                    technology = new Technology();
                    technology.setName(t);

                    team.addTechnology(technology);
                    this.technologyService.saveTechnology(technology);
                }
            }
            team.setOtherTechnologies(teamBindingModel.getOtherTechnologies());
        }

        user.setLeader(true);
        user.setTeam(team);
        user.setPhone(teamBindingModel.getPhone());

        Set<String> emails = new TreeSet<>();
        emails.add(teamBindingModel.getEmail2());
        emails.add(teamBindingModel.getEmail3());
        emails.add(teamBindingModel.getEmail4());
        emails.add(teamBindingModel.getEmail5());

        String appUrl = request.getScheme() + "://" + request.getServerName();

        for (String email : emails) {
            if(!email.isEmpty()) {
                User participant = this.userService.findByEmail(email);

                if(participant == null) {
                    redir.addFlashAttribute("message", NotificationMessages.USER_DOESNT_EXISTS(email));

                    return "redirect:/team/create";
                }

                if(participant.getTeam() != null) {
                    redir.addFlashAttribute("message", NotificationMessages.USER_ALREADY_HAS_A_TEAM(participant.getEmail()));

                    return "redirect:/team/create";
                }

                participant.setTeam(team);
                team.addUser(participant);

                SimpleMailMessage teamEmail = this.emailService.createEmail(
                        participant.getEmail(),
                        EmailDrafts.NEW_TEAM_USER_SUBJECT,
                        EmailDrafts.NEW_TEAM_USER_CONTENT(team.getName(), appUrl),
                        "hacktues@elsys-bg.org"
                );

                emailService.sendEmail(teamEmail);
            }
        }

        SimpleMailMessage teamEmail = this.emailService.createEmail(
                user.getEmail(),
                EmailDrafts.NEW_TEAM_LEADER_SUBJECT,
                EmailDrafts.NEW_TEAM_LEADER_CONTENT(team.getName(), appUrl),
                "hacktues@elsys-bg.org"
        );

        emailService.sendEmail(teamEmail);

        if(teamBindingModel.getParticipantsCount() < 5) {
            if(teamBindingModel.getWantTeammates() == null) {
                teamBindingModel.setWantTeammates(false);
            }

            team.setWantTeammates(teamBindingModel.getWantTeammates());
        }

        this.teamService.saveTeam(team);

        redir.addFlashAttribute("message", NotificationMessages.TEAM_SUCCESSFULLY_CREATED(team.getName()));

        return "redirect:/team";
    }*/

    @GetMapping("/team")
    @PreAuthorize("isAuthenticated()")
    public String teamPage(Model model) {
        UserDetails principal = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = this.userService.findByEmail(principal.getUsername());

        Team team = user.getTeam();

        if(team == null) {
            return "redirect:/team";
        }

        List<Technology> technologies = new ArrayList<>(team.getTechnologies());

        model.addAttribute("user", user);
        model.addAttribute("team", team);
        model.addAttribute("technologies", technologies);
        model.addAttribute("view", "team/team-page");

        return "base-layout";
    }

    @GetMapping("/teams")
    public String teams(Model model){
        List<Team> teams = this.teamService.getAllTeams();

        Collections.sort(teams, new Comparator<Team>(){
            public int compare(Team t1, Team t2){
                return t1.getName().compareTo(t2.getName());
            }
        });

        model.addAttribute("teams", teams);
        model.addAttribute("view", "info/teams");

        return "base-layout";
    }

    @GetMapping("/teams/{id}")
    public String teamDetails(@PathVariable Integer id, Model model, RedirectAttributes redir) {
        if(!this.teamService.teamExists(id)) {
            redir.addFlashAttribute("message", NotificationMessages.TEAM_DOESNT_EXIST);

            return "redirect:/teams";
        }

        if(SecurityContextHolder.getContext().getAuthentication() != null
                && SecurityContextHolder.getContext().getAuthentication().isAuthenticated()
                && !(SecurityContextHolder.getContext().getAuthentication() instanceof AnonymousAuthenticationToken)) {

            UserDetails principal = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            User user = this.userService.findByEmail(principal.getUsername());

            model.addAttribute("user", user);
        }

        Team team = this.teamService.findById(id);
        List<Technology> technologies = new ArrayList<>(team.getTechnologies());

        model.addAttribute("team", team);
        model.addAttribute("technologies", technologies);
        model.addAttribute("view", "info/team-details");

        return "base-layout";
    }

    @GetMapping("/finalists")
    public String finalists(Model model){
        List<Team> finalists = this.teamService.getAllTeams().stream().filter((finalist) -> finalist.isFinalist()).collect(Collectors.toList());

        Team winner1 = this.teamService.getAllTeams().stream().filter((finalist) -> finalist.getPlace() == 1).collect(Collectors.toList()).get(0);

        Team winner2 = this.teamService.getAllTeams().stream().filter((finalist) -> finalist.getPlace() == 2).collect(Collectors.toList()).get(0);

        Team winner3 = this.teamService.getAllTeams().stream().filter((finalist) -> finalist.getPlace() == 3).collect(Collectors.toList()).get(0);

        Collections.sort(finalists, new Comparator<Team>(){
            public int compare(Team t1, Team t2){
                return t1.getName().compareTo(t2.getName());
            }
        });

        model.addAttribute("winner1", winner1);
        model.addAttribute("winner2", winner2);
        model.addAttribute("winner3", winner3);

        model.addAttribute("finalists", finalists);
        model.addAttribute("view", "info/finalists");

        return "base-layout";
    }

    /*@GetMapping("/team/pick/mentor/{id}")
    @PreAuthorize("isAuthenticated()")
    public String pickMentor(@PathVariable Integer id, Model model, RedirectAttributes redir) {
        if(!this.mentorService.mentorExists(id)) {
            redir.addFlashAttribute("message", NotificationMessages.MENTOR_DOESNT_EXIST);

            return "redirect:/mentors";
        }

        UserDetails principal = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = this.userService.findByEmail(principal.getUsername());

        Team team = user.getTeam();

        if(team == null) {
            redir.addFlashAttribute("message", NotificationMessages.ACCESS_DENIED);

            return "redirect:/team";
        }

        if(!user.isLeader()) {
            redir.addFlashAttribute("message", NotificationMessages.ACCESS_DENIED);

            return "redirect:/team";
        }

        if(team.getMentor() != null) {
            redir.addFlashAttribute("message", NotificationMessages.TEAM_ALREADY_HAS_MENTOR);

            return "redirect:/team";
        }

        Mentor mentor = this.mentorService.findMentor(id);

        if(mentor.getTeamCount() <= 0) {
            redir.addFlashAttribute("message", NotificationMessages.MENTOR_TAKEN);

            return "redirect:/mentors";
        }

        model.addAttribute("mentor", mentor);
        model.addAttribute("view", "team/mentor-pick");

        return "base-layout";
    }*/

    /*@PostMapping("/team/pick/mentor/{id}")
    @PreAuthorize("isAuthenticated()")
    public String pickMentorProcess(@PathVariable Integer id, RedirectAttributes redir, HttpServletRequest request) {
        if(!this.mentorService.mentorExists(id)) {
            redir.addFlashAttribute("message", NotificationMessages.MENTOR_DOESNT_EXIST);

            return "redirect:/mentors";
        }

        UserDetails principal = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = this.userService.findByEmail(principal.getUsername());

        Team team = user.getTeam();

        if(team == null) {
            redir.addFlashAttribute("message", NotificationMessages.ACCESS_DENIED);

            return "redirect:/team";
        }

        if(!user.isLeader()) {
            redir.addFlashAttribute("message", NotificationMessages.ACCESS_DENIED);

            return "redirect:/team";
        }

        if(team.getMentor() != null) {
            redir.addFlashAttribute("message", NotificationMessages.TEAM_ALREADY_HAS_MENTOR);

            return "redirect:/team";
        }

        Mentor mentor = this.mentorService.findMentor(id);

        if(mentor.getTeamCount() <= 0) {
            redir.addFlashAttribute("message", NotificationMessages.MENTOR_TAKEN);

            return "redirect:/mentors";
        }

        team.setMentor(mentor);
        mentor.addTeam(team);

        mentor.setTeamCount(mentor.getTeamCount() - 1);

        this.teamService.saveTeam(team);
        this.mentorService.saveMentor(mentor);

        redir.addFlashAttribute("message", NotificationMessages.TEAM_MENTOR_SUCCESSFULLY_PICKED);

        return "redirect:/team";
    }*/

    /*@GetMapping("/team/delete/mentor/{id}")
    @PreAuthorize("isAuthenticated()")
    public String deleteMentor(@PathVariable Integer id, Model model, RedirectAttributes redir) {
        if(!this.mentorService.mentorExists(id)) {
            redir.addFlashAttribute("message", NotificationMessages.MENTOR_DOESNT_EXIST);

            return "redirect:/mentors";
        }

        UserDetails principal = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = this.userService.findByEmail(principal.getUsername());

        Team team = user.getTeam();

        if(team == null) {
            redir.addFlashAttribute("message", NotificationMessages.ACCESS_DENIED);

            return "redirect:/team";
        }

        if(!user.isLeader()) {
            redir.addFlashAttribute("message", NotificationMessages.ACCESS_DENIED);

            return "redirect:/team";
        }

        if(team.getMentor() == null) {
            redir.addFlashAttribute("message", NotificationMessages.TEAM_DOESNT_HAS_MENTOR);

            return "redirect:/mentors";
        }

        if(id != team.getMentor().getId()) {
            redir.addFlashAttribute("message", NotificationMessages.ACCESS_DENIED);

            return "redirect:/team";
        }

        Mentor mentor = this.mentorService.findMentor(id);

        model.addAttribute("mentor", mentor);
        model.addAttribute("view", "team/mentor-delete");

        return "base-layout";
    }*/

    /*@PostMapping("/team/delete/mentor/{id}")
    @PreAuthorize("isAuthenticated()")
    public String deleteMentorProcess(@PathVariable Integer id, RedirectAttributes redir, HttpServletRequest request) {
        if(!this.mentorService.mentorExists(id)) {
            redir.addFlashAttribute("message", NotificationMessages.MENTOR_DOESNT_EXIST);

            return "redirect:/mentors";
        }

        UserDetails principal = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = this.userService.findByEmail(principal.getUsername());

        Team team = user.getTeam();

        if(team == null) {
            redir.addFlashAttribute("message", NotificationMessages.ACCESS_DENIED);

            return "redirect:/team";
        }

        if(!user.isLeader()) {
            redir.addFlashAttribute("message", NotificationMessages.ACCESS_DENIED);

            return "redirect:/team";
        }

        if(team.getMentor() == null) {
            redir.addFlashAttribute("message", NotificationMessages.TEAM_DOESNT_HAS_MENTOR);

            return "redirect:/mentors";
        }

        if(id != team.getMentor().getId()) {
            redir.addFlashAttribute("message", NotificationMessages.ACCESS_DENIED);

            return "redirect:/team";
        }

        Mentor mentor = this.mentorService.findMentor(id);

        team.setMentor(null);
        mentor.deleteTeam(team);

        mentor.setTeamCount(mentor.getTeamCount() + 1);

        this.teamService.saveTeam(team);
        this.mentorService.saveMentor(mentor);

        redir.addFlashAttribute("message", NotificationMessages.TEAM_MENTOR_SUCCESSFULLY_DELETED);

        return "redirect:/team";
    }*/

    /*@GetMapping("/team/form")
    @PreAuthorize("isAuthenticated()")
    public String allTeams(Model model) {
        UserDetails principal = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = this.userService.findByEmail(principal.getUsername());

        List<Team> teams = this.teamService.getAllTeams().stream().filter(Team::getWantTeammates).filter(t -> t.getUsers().size() < 5).collect(Collectors.toList());
        List<User> users = this.userService.getAllUsers().stream().filter(u -> u.getTeam() == null).collect(Collectors.toList());

        model.addAttribute("teams", teams);
        model.addAttribute("users", users);
        model.addAttribute("view", "team/form-team");

        return "base-layout";
    }*/

    /*@GetMapping("/team/user/add")
    @PreAuthorize("isAuthenticated()")
    public String addUser(Model model, RedirectAttributes redir) {
        UserDetails principal = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = this.userService.findByEmail(principal.getUsername());

        if(!user.isLeader()) {
            redir.addFlashAttribute("message", NotificationMessages.ACCESS_DENIED);

            return "redirect:/team";
        }

        Team team = user.getTeam();

        if(team == null) {
            return "redirect:/team/create";
        }

        if(team.getUsers().size() == 5) {
            redir.addFlashAttribute("message", NotificationMessages.MAXIMAL_PARTICIPANTS_COUNT);

            return "redirect:/team";
        }

        model.addAttribute("view", "team/add-user");

        return "base-layout";
    }

    @PostMapping("/team/user/add")
    @PreAuthorize("isAuthenticated()")
    public String addUserProcess(UserEmailBindingModel userEmailBindingModel, BindingResult bindingResult, RedirectAttributes redir, HttpServletRequest request) {
        if (bindingResult.hasErrors()) {
            redir.addFlashAttribute("message", NotificationMessages.FILL_FORM_CORRECTLY);

            return "redirect:/team/user/add";
        }

        UserDetails principal = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = this.userService.findByEmail(principal.getUsername());

        if(!user.isLeader()) {
            redir.addFlashAttribute("message", NotificationMessages.ACCESS_DENIED);

            return "redirect:/team";
        }

        Team team = user.getTeam();

        if(team == null) {
            redir.addFlashAttribute("message", NotificationMessages.USER_CREATE_TEAM_FIRST);

            return "redirect:/team/create";
        }

        if(team.getUsers().size() == 5) {
            redir.addFlashAttribute("message", NotificationMessages.MAXIMAL_PARTICIPANTS_COUNT);

            return "redirect:/team";
        }

        String email = userEmailBindingModel.getEmail();

        User participant = this.userService.findByEmail(email);

        if(participant == null) {
            redir.addFlashAttribute("message", NotificationMessages.USER_DOESNT_EXISTS(email));

            return "redirect:/team/user/add";
        }

        if(participant.getTeam() != null) {
            if(participant.getTeam() == team) {
                redir.addFlashAttribute("message", NotificationMessages.USER_ALREADY_IN_SAME_TEAM(participant.getEmail()));

                return "redirect:/team/user/add";
            } else {
                redir.addFlashAttribute("message", NotificationMessages.USER_ALREADY_HAS_A_TEAM(participant.getEmail()));

                return "redirect:/team/user/add";
            }
        }

        participant.setTeam(team);
        team.addUser(participant);

        if(team.getUsers().size() == 5) {
            team.setWantTeammates(false);
        }

        this.teamService.saveTeam(team);

        String appUrl = request.getScheme() + "://" + request.getServerName();

        SimpleMailMessage teamEmail = this.emailService.createEmail(
                participant.getEmail(),
                EmailDrafts.NEW_USER_TO_TEAM_SUBJECT,
                EmailDrafts.NEW_USER_TO_TEAM_CONTENT(team.getName(), appUrl),
                "hacktues@elsys-bg.org"
        );

        emailService.sendEmail(teamEmail);

        redir.addFlashAttribute("message", NotificationMessages.TEAM_USER_SUCCESSFULLY_ADDED);

        return "redirect:/team";
    }*/

    /*@GetMapping("/team/user/delete")
    @PreAuthorize("isAuthenticated()")
    public String deleteUser(Model model, RedirectAttributes redir) {
        UserDetails principal = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = this.userService.findByEmail(principal.getUsername());

        if(!user.isLeader()) {
            redir.addFlashAttribute("message", NotificationMessages.ACCESS_DENIED);

            return "redirect:/team";
        }

        Team team = user.getTeam();

        if(team == null) {
            redir.addFlashAttribute("message", NotificationMessages.USER_CREATE_TEAM_FIRST);

            return "redirect:/team/create";
        }

        if(team.getUsers().size() == 3) {
            redir.addFlashAttribute("message", NotificationMessages.MINIMAL_PARTICIPANTS_COUNT);

            return "redirect:/team";
        }

        model.addAttribute("view", "team/delete-user");

        return "base-layout";
    }

    @PostMapping("/team/user/delete")
    @PreAuthorize("isAuthenticated()")
    public String deleteUserProcess(UserEmailBindingModel userEmailBindingModel, BindingResult bindingResult, RedirectAttributes redir, HttpServletRequest request) {
        if (bindingResult.hasErrors()) {
            redir.addFlashAttribute("message", NotificationMessages.FILL_FORM_CORRECTLY);

            return "redirect:/team/user/delete";
        }

        UserDetails principal = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = this.userService.findByEmail(principal.getUsername());

        if(!user.isLeader()) {
            redir.addFlashAttribute("message", NotificationMessages.ACCESS_DENIED);

            return "redirect:/team";
        }

        Team team = user.getTeam();

        if(team == null) {
            redir.addFlashAttribute("message", NotificationMessages.USER_CREATE_TEAM_FIRST);

            return "redirect:/team/create";
        }

        if(team.getUsers().size() == 3) {
            redir.addFlashAttribute("message", NotificationMessages.MINIMAL_PARTICIPANTS_COUNT);

            return "redirect:/team";
        }

        String email = userEmailBindingModel.getEmail();

        User participant = this.userService.findByEmail(email);

        if(participant == null) {
            redir.addFlashAttribute("message", NotificationMessages.USER_DOESNT_EXISTS(email));

            return "redirect:/team/user/delete";
        }

        if(participant == user) {
            redir.addFlashAttribute("message", NotificationMessages.USER_LEADER_CANT_LEAVE_TEAM);

            return "redirect:/team/user/delete";
        }

        if(participant.getTeam() != user.getTeam()) {
            redir.addFlashAttribute("message", NotificationMessages.USER_NOT_IN_THIS_TEAM(participant.getEmail()));

            return "redirect:/team/user/delete";
        }

        participant.setTeam(null);
        team.deleteUser(participant);

        this.teamService.saveTeam(team);
        this.userService.saveUser(participant);

        SimpleMailMessage teamEmail = this.emailService.createEmail(
                participant.getEmail(),
                EmailDrafts.USER_DELETED_FROM_TEAM_SUBJECT,
                EmailDrafts.USER_DELETED_FROM_TEAM_CONTENT(team.getName(), team.getLeader().getFullName()),
                "hacktues@elsys-bg.org"
        );

        emailService.sendEmail(teamEmail);

        redir.addFlashAttribute("message", NotificationMessages.TEAM_USER_SUCCESSFULLY_DELETED);

        return "redirect:/team";
    }*/

    /*@GetMapping("/team/user/leave")
    @PreAuthorize("isAuthenticated()")
    public String leaveTeam(Model model, RedirectAttributes redir) {
        UserDetails principal = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = this.userService.findByEmail(principal.getUsername());

        if(user.isLeader()) {
            redir.addFlashAttribute("message", NotificationMessages.ACCESS_DENIED);

            return "redirect:/team";
        }

        Team team = user.getTeam();

        if(team == null) {
            redir.addFlashAttribute("message", NotificationMessages.USER_CREATE_TEAM_FIRST);

            return "redirect:/team/create";
        }

        if(team.getUsers().size() == 3) {
            redir.addFlashAttribute("message", NotificationMessages.USER_CANT_LEAVE_TEAM + " " + NotificationMessages.MINIMAL_PARTICIPANTS_COUNT);

            return "redirect:/team";
        }

        model.addAttribute("view", "team/leave-team");

        return "base-layout";
    }

    @PostMapping("/team/user/leave")
    @PreAuthorize("isAuthenticated()")
    public String leaveTeamProcess(RedirectAttributes redir, HttpServletRequest request) {
        UserDetails principal = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = this.userService.findByEmail(principal.getUsername());

        if(user.isLeader()) {
            redir.addFlashAttribute("message", NotificationMessages.ACCESS_DENIED);

            return "redirect:/team";
        }

        Team team = user.getTeam();

        if(team == null) {
            redir.addFlashAttribute("message", NotificationMessages.USER_CREATE_TEAM_FIRST);

            return "redirect:/team/create";
        }

        if(team.getUsers().size() == 3) {
            redir.addFlashAttribute("message", NotificationMessages.USER_CANT_LEAVE_TEAM + " " + NotificationMessages.MINIMAL_PARTICIPANTS_COUNT);

            return "redirect:/team";
        }

        team.deleteUser(user);
        user.setTeam(null);

        this.teamService.saveTeam(team);
        this.userService.saveUser(user);

        SimpleMailMessage teamEmail = this.emailService.createEmail(
                user.getEmail(),
                EmailDrafts.USER_LEFT_TEAM_USER_SUBJECT,
                EmailDrafts.USER_LEFT_TEAM_USER_CONTENT(team.getName(), team.getLeader().getFullName()),
                "hacktues@elsys-bg.org"
        );

        emailService.sendEmail(teamEmail);

        teamEmail = this.emailService.createEmail(
                team.getLeader().getEmail(),
                EmailDrafts.USER_LEFT_TEAM_LEADER_SUBJECT,
                EmailDrafts.USER_LEFT_TEAM_LEADER_CONTENT(user.getEmail()),
                "hacktues@elsys-bg.org"
        );

        emailService.sendEmail(teamEmail);

        redir.addFlashAttribute("message", NotificationMessages.USER_TEAM_SUCCESSFULLY_LEFT);

        return "redirect:/team/create";
    }*/

    /*@GetMapping("/team/delete")
    @PreAuthorize("isAuthenticated()")
    public String deleteTeam(Model model, RedirectAttributes redir) {
        UserDetails principal = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = this.userService.findByEmail(principal.getUsername());

        if(!user.isLeader()) {
            redir.addFlashAttribute("message", NotificationMessages.ACCESS_DENIED);

            return "redirect:/team";
        }

        Team team = user.getTeam();

        if(team == null) {
            redir.addFlashAttribute("message", NotificationMessages.USER_CREATE_TEAM_FIRST);

            return "redirect:/team/delete";
        }

        model.addAttribute("view", "team/delete-team");

        return "base-layout";
    }

    @PostMapping("/team/delete")
    @PreAuthorize("isAuthenticated()")
    public String deleteTeamProcess(RedirectAttributes redir, HttpServletRequest request) {
        UserDetails principal = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = this.userService.findByEmail(principal.getUsername());

        if(!user.isLeader()) {
            redir.addFlashAttribute("message", NotificationMessages.ACCESS_DENIED);

            return "redirect:/team";
        }

        Team team = user.getTeam();

        if(team == null) {
            redir.addFlashAttribute("message", NotificationMessages.USER_CREATE_TEAM_FIRST);

            return "redirect:/team/create";
        }

        for(User teammate : team.getUsers()) {
            teammate.setTeam(null);

            SimpleMailMessage teamEmail = this.emailService.createEmail(
                    teammate.getEmail(),
                    EmailDrafts.TEAM_DELETE_SUBJECT,
                    EmailDrafts.TEAM_DELETE_CONTENT(team.getName(), team.getLeader().getEmail()),
                    "hacktues@elsys-bg.org"
            );

            emailService.sendEmail(teamEmail);
        }

        user.setLeader(false);
        team.setLeader(null);

        this.teamService.deleteTeam(team);

        redir.addFlashAttribute("message", NotificationMessages.USER_LEADER_TEAM_SUCCESSFULLY_DELETED);

        return "redirect:/team";
    }*/

    /*@GetMapping("/team/leader/change")
    @PreAuthorize("isAuthenticated()")
    public String changeLeader(Model model, RedirectAttributes redir) {
        UserDetails principal = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = this.userService.findByEmail(principal.getUsername());

        if(!user.isLeader()) {
            redir.addFlashAttribute("message", NotificationMessages.ACCESS_DENIED);

            return "redirect:/team";
        }

        Team team = user.getTeam();

        if(team == null) {
            redir.addFlashAttribute("message", NotificationMessages.USER_CREATE_TEAM_FIRST);

            return "redirect:/team/create";
        }

        model.addAttribute("view", "team/change-leader");

        return "base-layout";
    }

    @PostMapping("/team/leader/change")
    @PreAuthorize("isAuthenticated()")
    public String changeLeaderProcess(UserEmailBindingModel userEmailBindingModel, BindingResult bindingResult, RedirectAttributes redir, HttpServletRequest request) {
        if (bindingResult.hasErrors()) {
            redir.addFlashAttribute("message", NotificationMessages.FILL_FORM_CORRECTLY);

            return "redirect:/team/leader/change";
        }

        UserDetails principal = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = this.userService.findByEmail(principal.getUsername());

        if(!user.isLeader()) {
            redir.addFlashAttribute("message", NotificationMessages.ACCESS_DENIED);

            return "redirect:/team";
        }

        String email = userEmailBindingModel.getEmail();
        User newLeader = this.userService.findByEmail(email);

        Team team = user.getTeam();

        if(newLeader == null) {
            redir.addFlashAttribute("message", NotificationMessages.USER_DOESNT_EXISTS(email));

            return "redirect:/team/leader/change";
        }

        if(newLeader.getTeam() != user.getTeam()) {
            redir.addFlashAttribute("message", NotificationMessages.USER_NOT_IN_THIS_TEAM(newLeader.getEmail()));

            return "redirect:/team/leader/change";
        }

        newLeader.setLeader(true);
        user.setLeader(false);
        team.setLeader(newLeader);

        this.userService.saveUser(newLeader);
        this.userService.saveUser(user);
        this.teamService.saveTeam(team);

        String appUrl = request.getScheme() + "://" + request.getServerName();

        SimpleMailMessage teamEmail = this.emailService.createEmail(
                user.getEmail(),
                EmailDrafts.TEAM_LEADER_CHANGE_LEADER_SUBJECT,
                EmailDrafts.TEAM_LEADER_CHANGE_LEADER_CONTENT(team.getName(), appUrl),
                "hacktues@elsys-bg.org"
        );
        emailService.sendEmail(teamEmail);

        teamEmail = this.emailService.createEmail(
                newLeader.getEmail(),
                EmailDrafts.TEAM_LEADER_CHANGE_USER_SUBJECT,
                EmailDrafts.TEAM_LEADER_CHANGE_USER_CONTENT(team.getName(), appUrl),
                "hacktues@elsys-bg.org"
        );
        emailService.sendEmail(teamEmail);

        redir.addFlashAttribute("message", NotificationMessages.USER_LEADER_TEAM_LEADER_SUCCESSFULLY_CHANGED);

        return "redirect:/team";
    }*/

    /*@GetMapping("/team/settings")
    @PreAuthorize("isAuthenticated()")
    public String teamSettings(Model model, RedirectAttributes redir) {
        UserDetails principal = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = this.userService.findByEmail(principal.getUsername());

        Team team = user.getTeam();

        if(!user.isLeader()) {
            redir.addFlashAttribute("message", NotificationMessages.ACCESS_DENIED);

            return "redirect:/team";
        }

        List<Technology> technologies = this.technologyService.getAllTechnologies();

        model.addAttribute("view", "team/settings");
        model.addAttribute("team", team);
        model.addAttribute("technologies", technologies);

        return "base-layout";
    }

    @PostMapping("/team/settings")
    @PreAuthorize("isAuthenticated()")
    public String teamSettingsProcess(TeamSettingsBindingModel teamSettingsBindingModel, RedirectAttributes redir) {
        UserDetails principal = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = this.userService.findByEmail(principal.getUsername());

        Team team = user.getTeam();

        if(!user.isLeader()) {
            redir.addFlashAttribute("message", NotificationMessages.ACCESS_DENIED);

            return "redirect:/team";
        }

        if(!(teamSettingsBindingModel.getTeamName().equals(team.getName())) && this.teamService.findTeam(teamSettingsBindingModel.getTeamName()) != null) {
            redir.addFlashAttribute("message", NotificationMessages.TEAM_ALREADY_EXISTS);

            return "redirect:/team";
        }

        if(teamSettingsBindingModel.getTechnologies() == null) {
            redir.addFlashAttribute("message", NotificationMessages.FILL_ALL_FIELDS);

            return "redirect:/team/settings";
        }

        if(teamSettingsBindingModel.getWantTeammates() == null) {
            teamSettingsBindingModel.setWantTeammates(false);
        }

        if(teamSettingsBindingModel.getTeamName().equals(team.getName()) &&
                teamSettingsBindingModel.getGitHubRepoUrl().equals(team.getGitHubRepoUrl()) &&
                teamSettingsBindingModel.getWantTeammates().equals(team.getWantTeammates()) &&
                teamSettingsBindingModel.getOtherTechnologies().equals(team.getOtherTechnologies())) {
            List<String> technologiesNames = new ArrayList<>();

            for(Technology t : team.getTechnologies()) {
                technologiesNames.add(t.getName());
            }

            if(teamSettingsBindingModel.getTechnologies().equals(technologiesNames) && teamSettingsBindingModel.getOtherTechnologies().isEmpty()) {
                return "redirect:/team";
            }
        }

        team.setName(teamSettingsBindingModel.getTeamName());
        team.setGitHubRepoUrl(teamSettingsBindingModel.getGitHubRepoUrl());
        team.setWantTeammates(teamSettingsBindingModel.getWantTeammates());

        team.setTechnologies(new TreeSet<>(new Comparator<Technology>() {
            @Override
            public int compare(Technology technology1, Technology technology2) {
                String name1 = technology1.getName().toLowerCase();
                String name2 = technology2.getName().toLowerCase();
                return name1.compareTo(name2);
            }
        }));

        for(String t : teamSettingsBindingModel.getTechnologies()) {
            Technology technology = this.technologyService.findTechnology(t);

            if(technology == null) {
                redir.addFlashAttribute("message", NotificationMessages.OTHER_TECHNOLOGIES);
                return "redirect:/team/settings";
            }

            team.addTechnology(technology);
        }

        team.setOtherTechnologies(null);

        if(!teamSettingsBindingModel.getOtherTechnologies().isEmpty()) {
            /*for (String t : teamSettingsBindingModel.getOtherTechnologies().trim().split("\\s*,\\s*")) {
                Technology technology = this.technologyService.findTechnology(t);

                if(technology == null) {
                    technology = new Technology();
                    technology.setName(t);

                    team.addTechnology(technology);
                    this.technologyService.saveTechnology(technology);
                }
            }

            team.setOtherTechnologies(teamSettingsBindingModel.getOtherTechnologies());
        }

        this.teamService.saveTeam(team);

        redir.addFlashAttribute("message", NotificationMessages.CHANGES_SAVED);

        return "redirect:/team";
    }*/
}
