package hacktuesApp.services;

public final class NotificationMessages {
    // USER MESSAGES
    public static final String USER_ALREADY_EXISTS = "Вече има потребител с предоставения имейл адрес! Сигурени ли сте, че не сте забравили паролата си?";

    public static final String USER_DOESNT_HAVE_FORGOT_PASSWORD_CONFIRMATION_TOKEN = "Първо трябва да въведете имейл адреса си!";

    public static final String USER_HAS_TEAM = "Вие имате отбор! Не можете да участвате в няколко отбора едновременно.";

    public static final String USER_CREATE_TEAM_FIRST = "Моля, първо създайте отбор!";

    public static final String USER_CANT_LEAVE_TEAM = "Не може да напуснете отбора!";

    public static final String USER_LEADER_CANT_LEAVE_TEAM = "Вие сте капитан, няма как да напуснете отбора си, те разчитат на вас!";

    public static final String USER_CONFIRMATION_EMAIL_SENT(String email) {
        return "Имейл за потвърждаване беше изпратен на " + email + ".";
    }

    public static final String USER_DOESNT_EXISTS(String email) {
        return "Потребител с имейл адрес \"" + email + "\" не съществува! Моля, регистрирайте го.";
    }

    public static final String USER_ALREADY_HAS_A_TEAM(String email) {
        return "Потребител с имейл адрес \"" + email + "\" вече има отбор!";
    }

    public static final String USER_ALREADY_IN_SAME_TEAM(String email) {
        return "Потребител с имейл адрес \"" + email + "\" е вече в отбора!";
    }

    public static final String USER_NOT_IN_THIS_TEAM(String email) {
        return "Потребител с имейл адрес \"" + email + "\" не е в отбора!";
    }

    public static final String USER_FORGOT_PASSWORD_CONFIRMATION_EMAIL_SENT(String email) {
        return "Имейл за потвърждаване беше изпратен на " + email;
    }

    // TEAM MESSAGES
    public static final String TEAM_ALREADY_EXISTS = "Дайте малко по-креативно. Вече има отбор със същото име!";

    public static final String TEAM_ALREADY_HAS_MENTOR = "Вашият отбор вече има ментор!";

    public static final String TEAM_DOESNT_HAS_MENTOR = "Вашият отбор все още няма ментор! Моля, първо изберете такъв!";

    public static final String TEAM_DOESNT_EXIST = "Търсеният отбор не съществува!";

    // SUCCESS MESSAGES
    public static final String USER_SUCCESSFULLY_LOGGED_OUT = "Успешно излязохте от профила си.";

    public static final String TEAM_USER_SUCCESSFULLY_ADDED = "Участникът бе добавен в отбора успешно!";

    public static final String TEAM_USER_SUCCESSFULLY_DELETED = "Участникът бе изтрит от отбора успешно!";

    public static final String USER_TEAM_SUCCESSFULLY_LEFT = "Успешно напуснахте отбор!";

    public static final String USER_LEADER_TEAM_SUCCESSFULLY_DELETED = "Успешно изтрихте отбор! Надяваме се, че не сте се отказали от хакатона.";

    public static final String USER_LEADER_TEAM_LEADER_SUCCESSFULLY_CHANGED = "Успешно сменихте капитана на отбора си!";

    public static final String BLOG_POST_SUCCESSFULLY_CREATED = "Публикацията е създадена успешно!";

    public static final String USER_EMAIL_SUCCESSFULLY_CONFIRMED = "Имейл адресът Ви е успешно потвърден! Моля, влезте в профила си.";

    public static final String TEAM_SUCCESSFULLY_CREATED(String name) {
        return "Отбор \"" + name + "\" е успешно създаден. Поздравления! Вече официално сте част от отборите на Hack 30xTUES!";
    }

    public static final String TEAM_MENTOR_SUCCESSFULLY_PICKED = "Успешно избрахте ментор за отбора Ви!";

    public static final String TEAM_MENTOR_SUCCESSFULLY_DELETED = "Успешно премахнахте ментора на отбора Ви!";

    // FORM MESSAGES
    public static final String FILL_FORM_CORRECTLY = "Моля, попълнете формата правилно!";

    public static final String FILL_ALL_FIELDS = "Моля, попълнете всички полета!";

    public static final String OTHER_TECHNOLOGIES = "Моля, добавете други технологии в полето отдолу!";

    public static final String MINIMAL_AND_MAXIMAL_PARTICIPANTS_COUNT = "Минималният брой участници в един отбор е 3, а максималният - 5!";

    public static final String CANT_USE_EMAIL_MULTIPLE_TIMES = "Не може да използвате един и същ имейл адрес за повече от един участник!";

    public static final String MAXIMAL_PARTICIPANTS_COUNT = "Надвишихте лимита. Максималният брой участници в отбор е 5!";

    public static final String MINIMAL_PARTICIPANTS_COUNT = "Минималният брой участници е 3! Добавете още приятели, моля.";

    public static final String CHANGES_SAVED = "Промените бяха запазени! Надяваме се.";

    public static final String PASSWORDS_DONT_MATCH = "Паролите не съвпадат. Пробвайте пак.";

    public static final String PASSWORD_TOO_WEAK = "Паролата ти е твърде слаба. Избери по-надеждна.";

    public static final String WRONG_EMAIL_OR_PASSWORD = "Невалиден имейл или парола! Пробвай пак.";

    public static final String WRONG_SCHOOL_CLASS = "Участниците в HackTUES трябва да са от " + FormInformationService.schoolClasses + " клас!";

    public static final String WRONG_DIET_CHOICE = "За съжаление не предлагаме такъв вид храна, не сме ресторант. Възможните предпочитания към храната са " + FormInformationService.diets + "!";

    public static final String WRONG_TSHIRT_SIZE = "Грешен размер. Размерите на тениските могат да са " + FormInformationService.tshirts + "!";

    // URL MESSAGES
    public static final String ACCESS_DENIED = "За съжаление нямате достъп до този адрес!";

    public static final String WRONG_CONFIRMATION_LINK = "Опа! Нещо се счупи. Грешен линк за потвърждаване на имейл."; //not very likely

    // BLOG POST MESSAGES
    public static final String BLOG_POST_DOESNT_EXIST = "Търсената публикация не съществува!";

    // MENTOR MESSAGES
    public static final String MENTOR_DOESNT_EXIST = "Търсеният ментор не съществува!";

    public static final String MENTOR_TAKEN = "Избраният ментор вече има максималния брой отбори! Моля, изберете друг.";

    // PRIVATE //
    /*
     The caller references the constants using <tt>NotificationMessages.EMPTY_STRING</tt>,
     and so on. Thus, the caller should be prevented from constructing objects of
     this class, by declaring this private constructor.
     */
    private NotificationMessages(){
        //this prevents even the native class from
        //calling this ctor as well :
        throw new AssertionError();
    }
}