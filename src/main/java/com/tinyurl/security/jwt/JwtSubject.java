/**
 *
 */
package com.tinyurl.security.jwt;

import com.tinyurl.security.users.User;
import io.jsonwebtoken.lang.Assert;
import lombok.Data;

@Data
public class JwtSubject {
    private long tokenCreation;
    private String username;
    private String authorities;


    public JwtSubject(User user, String authorities) {
        Assert.notNull(user, "cannot create a JwtSubject without a user");
        this.username = user.getUserName();
        this.authorities = authorities;
        this.tokenCreation = System.currentTimeMillis();
    }

    public JwtSubject(String username) {
        Assert.notNull(username, "cannot create a JwtSubject without a username");
        this.username = username;
        this.authorities = authorities;
        this.tokenCreation = System.currentTimeMillis();
    }
}
