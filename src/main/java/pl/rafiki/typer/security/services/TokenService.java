package pl.rafiki.typer.security.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Service;
import pl.rafiki.typer.user.User;
import pl.rafiki.typer.user.UserService;

import java.time.Instant;
import java.util.stream.Collectors;

@Service
public class TokenService {
    @Autowired
    private JwtEncoder jwtEncoder;
    @Autowired
    private JwtDecoder jwtDecoder;
    @Value("${typer.app.jwtExpirationSec}")
    private Long jwtTokenValidityTime;

    public Jwt generateJwt(Authentication auth) {
        Instant now = Instant.now();

        String scope = auth.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(" "));

        Object principal = auth.getPrincipal();
        Long userId = ((User) principal).getId();

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("self")
                .issuedAt(now)
                .subject(userId.toString())
                .claim("roles", scope)
                .expiresAt(Instant.now().plusSeconds(jwtTokenValidityTime))
                .build();

        return jwtEncoder.encode(JwtEncoderParameters.from(claims));
    }

    public Jwt generateJwtByUserId(Long userId, UserService userService) {
        Instant now = Instant.now();

        UserDetails userDetails = userService.loadUserByUserId(userId);
        String scope = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(" "));

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("self")
                .issuedAt(now)
                .subject(userId.toString())
                .claim("roles", scope)
                .expiresAt(Instant.now().plusSeconds(jwtTokenValidityTime))
                .build();

        return jwtEncoder.encode(JwtEncoderParameters.from(claims));
    }

    public Long getUserIdFromToken(String jwtToken) {
        return Long.parseLong(jwtDecoder.decode(jwtToken).getSubject());
    }
}
