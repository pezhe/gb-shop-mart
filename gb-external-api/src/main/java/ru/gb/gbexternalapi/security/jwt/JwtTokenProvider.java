package ru.gb.gbexternalapi.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import ru.gb.gbexternalapi.entity.security.AccountRole;

import javax.annotation.PostConstruct;
import java.util.Base64;
import java.util.Date;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class JwtTokenProvider {

    @Value("${jwt.token.secret}")
    private String secret;

    @Value("${jwt.token.expired}")
    private long validityInMilliseconds;

    private final UserDetailsService userDetailsService;

    @PostConstruct
    public void init() {
        secret = Base64.getEncoder().encodeToString(secret.getBytes());
    }

    public String createToken(String username, Set<AccountRole> roles) {
        Claims claims = Jwts.claims().setSubject(username);
        claims.put("roles", roles.stream().map(AccountRole::getName).collect(Collectors.toSet()));

        Date now = new Date();

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + validityInMilliseconds)) // todo
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();
    }
}
