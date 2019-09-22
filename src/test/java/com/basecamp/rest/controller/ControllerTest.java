package com.basecamp.rest.controller;

import com.basecamp.rest.config.WebSecurityConfig;
import com.basecamp.rest.domain.Role;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collection;
import java.util.Date;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public abstract class ControllerTest {
    protected String getToken(String key, Collection<Role> roles) {
        return Jwts.builder()
                .signWith(Keys.hmacShaKeyFor(key.getBytes()), SignatureAlgorithm.HS512)
                .setHeaderParam("typ", WebSecurityConfig.TOKEN_TYPE)
                .setSubject("admin")
                .setExpiration(new Date(System.currentTimeMillis() + 86400000))
                .claim("rol", roles)
                .compact();
    }
}
