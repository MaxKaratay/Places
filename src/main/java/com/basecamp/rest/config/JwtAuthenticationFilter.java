package com.basecamp.rest.config;

import com.basecamp.rest.domain.Person;
import com.basecamp.rest.exception.NotAuthenticatedException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Log4j2
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final AuthenticationManager authenticationManager;
    private final String secret;

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager, String secret) {
        this.authenticationManager = authenticationManager;
        this.secret = secret;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try {
            log.info("start auth");
            Person person = new ObjectMapper()
                    .readValue(request.getInputStream(), Person.class);
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(person.getName(), person.getPassword());
            return authenticationManager.authenticate(authenticationToken);
        } catch (IOException e) {
            log.error("Error trying authenticate user ", e);
            throw new NotAuthenticatedException(e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        Person person = (Person) authResult.getPrincipal();
        List<String> roles = person.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList());
        byte[] bytes = secret.getBytes();
        String token = Jwts.builder()
                .signWith(Keys.hmacShaKeyFor(bytes), SignatureAlgorithm.HS512)
                .setHeaderParam("typ", WebSecurityConfig.TOKEN_TYPE)
                .setSubject(person.getName())
                .setExpiration(new Date(System.currentTimeMillis() + 86400000))
                .claim("rol", roles)
                .compact();
        response.addHeader(WebSecurityConfig.TOKEN_HEADER, WebSecurityConfig.TOKEN_PREFIX + token);
    }
}
