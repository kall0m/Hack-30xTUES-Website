package hacktuesApp.services;

public final class EmailDrafts {
    // USER REGISTRATION
    public static final String USER_REGISTRATION_SUBJECT = "HackTUES - Регистрация";
    public static final String USER_REGISTRATION_CONTENT(String appUrl, String confirmationToken) {
        return "За да потвърдите имейл адреса си, моля натиснете линка отдолу:\n" + appUrl + "/confirm?token=" + confirmationToken + "\n\nПоздрави,\nhacktues@elsys-bg.org.";
    }

    // USER FORGOT PASSWORD
    public static final String USER_FORGOT_PASSWORD_SUBJECT = "HackTUES - Забравена парола";
    public static final String USER_FORGOT_PASSWORD_CONTENT(String appUrl, String forgotPasswordToken) {
        return "За да смените паролата си, моля последвайте линка отдолу:\n" + appUrl + "/user/set_new_password?token=" + forgotPasswordToken + "\n\nПоздрави,\nhacktues@elsys-bg.org.";
    }

    // NEW TEAM
    // INFORM USERS
    public static final String NEW_TEAM_USER_SUBJECT = "HackTUES - Добре дошли в новия си отбор!";
    public static final String NEW_TEAM_USER_CONTENT(String team, String appUrl) {
        return "Бяхте добавен/а в отбор: " + team + ".\n" + "Линк към отбора: " + appUrl + "/team" +
                "\nЗа да напуснете отбора, моля посетете следния линк: " + appUrl + "/team/user/leave" + "\n(Моля, първо влезте в профила си.)" + "\n\nПоздрави,\nhacktues@elsys-bg.org.";
    }
    // INFORM LEADER
    public static final String NEW_TEAM_LEADER_SUBJECT = "HackTUES - Добре дошли в новия си отбор!";
    public static final String NEW_TEAM_LEADER_CONTENT(String team, String appUrl) {
        return "Вие сте капитан на отбор: " + team + ".\n" + "Линк към отбора: " + appUrl + "/team" + "\n(Моля, първо влезте в профила си.)" + "\n\nПоздрави,\nhacktues@elsys-bg.org.";
    }

    // NEW USER TO TEAM
    public static final String NEW_USER_TO_TEAM_SUBJECT = "HackTUES - Добре дошли в новия си отбор!";
    public static final String NEW_USER_TO_TEAM_CONTENT(String team, String appUrl) {
        return "Бяхте добавен/а в отбор: " + team + ".\n" + "Линк към отбора: " + appUrl + "/team" +
                "\nЗа да напуснете отбора, моля посетете следния линк: " + appUrl + "/team/user/leave" + "\n(Моля, първо влезте в профила си.)" + "\n\nПоздрави,\nhacktues@elsys-bg.org.";
    }

    // USER DELETED FROM TEAM
    public static final String USER_DELETED_FROM_TEAM_SUBJECT = "HackTUES - Вече нямате отбор!";
    public static final String USER_DELETED_FROM_TEAM_CONTENT(String team, String leader) {
        return "Бяхте премахнат/а от отбор " + team + " с капитан " + leader +"." + "\n\nПоздрави,\nhacktues@elsys-bg.org.";
    }

    // USER LEFT TEAM
    // INFORM USER
    public static final String USER_LEFT_TEAM_USER_SUBJECT = "HackTUES - Вече нямате отбор!";
    public static final String USER_LEFT_TEAM_USER_CONTENT(String team, String leader) {
        return "Вие напуснахте отбор " + team + " с капитан " + leader +"." + "\n\nПоздрави,\nhacktues@elsys-bg.org.";
    }
    // INFORM LEADER
    public static final String USER_LEFT_TEAM_LEADER_SUBJECT = "HackTUES - Участник напусна отбора Ви!";
    public static final String USER_LEFT_TEAM_LEADER_CONTENT(String email) {
        return "Участник с имейл адрес " + email + " напусна вашия отбор." + "\n\nПоздрави,\nhacktues@elsys-bg.org.";
    }

    // TEAM DELETE
    public static final String TEAM_DELETE_SUBJECT = "HackTUES - Вече нямате отбор!";
    public static final String TEAM_DELETE_CONTENT(String team, String leader) {
        return "Отборът ви " + team + " беше изтрит от " + leader + "." + "\n\nПоздрави,\nhacktues@elsys-bg.org.";
    }

    // TEAM LEADER CHANGE
    // INFORM USER
    public static final String TEAM_LEADER_CHANGE_USER_SUBJECT = "HackTUES - Вече сте капитан!";
    public static final String TEAM_LEADER_CHANGE_USER_CONTENT(String team, String appUrl) {
        return "Вие вече сте капитан на отбор: " + team + ".\n" + "Линк към отбора: " + appUrl + "/team" + "\n(Моля, първо влезте в профила си.)\nКато капитан разчитаме на Вас да се свързвате с нас за всякакви проблеми и въпроси, свързани с участието на " + team + " в Hack 30xTUES." + "\n\nПоздрави,\nhacktues@elsys-bg.org.";
    }
    // INFORM LEADER
    public static final String TEAM_LEADER_CHANGE_LEADER_SUBJECT = "HackTUES - Вече не сте капитан!";
    public static final String TEAM_LEADER_CHANGE_LEADER_CONTENT(String team, String appUrl) {
        return "Вие се отказахте да сте капитан на отбор: " + team + ".\n" + "Линк към отбора: " + appUrl + "/team" + "\n(Моля, първо влезте в профила си.)" + "\n\nПоздрави,\nhacktues@elsys-bg.org.";
    }

    // NEW POST IN BLOG
    public static final String NEW_POST_IN_BLOG_SUBJECT = "HackTUES - Нова публикация в блога";
    public static final String NEW_POST_IN_BLOG_CONTENT(String user, String appUrl, Integer postId) {
        return "Нова информация от екипа на Hack TUES. За да разберете повече, моля, последвайте линка: " + appUrl + "/blog/post/" + postId + "\n\nКакво ли се крие в публикацията?" + "\n\nПоздрави,\nhacktues@elsys-bg.org.";
    }

    // PICKED MENTOR
    public static final String PICKED_MENTOR_SUBJECT = "HackTUES - Бяхте избран/а за ментор";
    public static final String PICKED_MENTOR_CONTENT(String team, String leader, Integer teamId, Integer teams, String appUrl, Integer postId) {
        return "Здравейте! Отбор " + team + "Ви избра за ментор. Имейл на капитан на отбора: " + leader + "." +
                "Линк към отбора: " + appUrl + "/teams/" + teamId + "\nВие сте ментор на " + teams + " отбора." + "\n\nПоздрави,\nhacktues@elsys-bg.org.";
    }

    // DELETED MENTOR
    public static final String DELETED_MENTOR_SUBJECT = "HackTUES - Отбор Ви премахна като ментор";
    public static final String DELETED_MENTOR_CONTENT(String team, String leader, Integer teamId, Integer teams, String appUrl, Integer postId) {
        return "Здравейте! Отбор " + team + "Ви премахна като ментор. Имейл на капитан на отбора: " + leader + "." +
                "Линк към отбора: " + appUrl + "/teams/" + teamId + "\nВие сте ментор на " + teams + " отбора." + "\n\nПоздрави,\nhacktues@elsys-bg.org.";
    }

    // PRIVATE //
    /*
     The caller references the constants using <tt>NotificationMessages.EMPTY_STRING</tt>,
     and so on. Thus, the caller should be prevented from constructing objects of
     this class, by declaring this private constructor.
     */
    private EmailDrafts(){
        //this prevents even the native class from
        //calling this ctor as well :
        throw new AssertionError();
    }
}
