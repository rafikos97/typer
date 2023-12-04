package pl.rafiki.typer.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterUserDTO{
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;
    @NotNull(message = "First name cannot be null")
    private String firstName;
    @NotNull(message = "Last name cannot be null")
    private String lastName;
    @NotNull(message = "Username cannot be null")
    private String username;
    @NotNull(message = "Email address cannot be null")
    private String email;
    @NotNull(message = "Password cannot be null")
    private String password;

    public RegisterUserDTO(String firstName, String lastName, String username, String email, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.email = email;
        this.password = password;
    }
}
