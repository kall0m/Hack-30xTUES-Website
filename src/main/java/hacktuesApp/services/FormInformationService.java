package hacktuesApp.services;

import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service("formInformationService")
public final class FormInformationService {
    // USER FORM INFORMATION

    public static final List<String> schoolClasses = Arrays.asList("8А", "8Б", "8В", "8Г",
                                                                        "9А", "9Б", "9В", "9Г",
                                                                        "10А", "10Б", "10В", "10Г",
                                                                        "11А", "11Б", "11В", "11Г",
                                                                        "12А", "12Б", "12В", "12Г"
    );

    public static final List<String> diets = Arrays.asList("no-preferences", "no-meat");

    public static final List<String> tshirts = Arrays.asList("XS", "S", "M", "L", "XL");
}
