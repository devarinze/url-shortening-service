package com.tinyurl.security.auth;

import com.tinyurl.security.users.User;
import com.tinyurl.security.users.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

@Component
public class AuthManager implements AuthenticationProvider {

    @Autowired
    private UserService userService;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public Authentication authenticate(Authentication a) throws AuthenticationException {
        logger.debug("Authenticated ? {}", a.isAuthenticated());
        if (a.isAuthenticated()) {
            logger.debug("Already authenticated. return same");
            return a;
        }
        String userName = (String) a.getPrincipal();
        logger.info("Authentication for : " + userName);
        User user = userService.loadUserByUsername(userName);
        if (user == null) {
            throw new BadCredentialsException("Invalid username");
        }
        return new UsernamePasswordAuthenticationToken(user, "");
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(BizAuthentication.class);
    }

}
