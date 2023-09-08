package pl.rafiki.typer.user;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import pl.rafiki.typer.security.models.Role;
import pl.rafiki.typer.security.repositories.RoleRepository;

import java.util.HashSet;
import java.util.Set;

@Configuration
public class UserConfig {

    @Bean
    CommandLineRunner run(RoleRepository roleRepository, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args ->  {
            if (roleRepository.findByAuthority("ADMIN").isPresent()) return;
            Role adminRole = roleRepository.save(new Role("ADMIN"));
            roleRepository.save(new Role("USER"));

            Set<Role> roles = new HashSet<>();
            roles.add(adminRole);

            User admin = new User("Admin", "Admin", "admin", "admin@gmail.com", passwordEncoder.encode("Admin1"), roles);

            userRepository.save(admin);
        };
    }
}
