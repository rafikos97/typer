package pl.rafiki.typer.user;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    private Long id;
    @NotNull(message = "First name cannot be null")
    private String firstName;
    @NotNull(message = "Last name cannot be null")
    private String lastName;
    @NotNull(message = "Username cannot be null")
    private String username;
    @NotNull(message = "Email address cannot be null")
    private String email;

    public UserDTO(String firstName, String lastName, String username, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.email = email;
    }
}
