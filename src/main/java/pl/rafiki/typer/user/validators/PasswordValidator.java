package pl.rafiki.typer.user.validators;

import org.springframework.stereotype.Component;

@Component
public class PasswordValidator {
    private static final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$";

    public static boolean isPasswordValid(String password) {
        return password.matches(PASSWORD_PATTERN);
    }
}
