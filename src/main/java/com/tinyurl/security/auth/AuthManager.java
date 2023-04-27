package com.tinyurl.security.auth;

import com.tinyurl.security.users.User;
import com.tinyurl.security.users.UserRepository;
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
    private UserRepository userRepository;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public Authentication authenticate(Authentication a) throws AuthenticationException {
        logger.debug("Authenticated ? {}", a.isAuthenticated());
        if (a.isAuthenticated()) {
            logger.debug("Already authenticated. return same");
            return a;
        }
        String userName = (String) a.getPrincipal();
        String password = (String) a.getCredentials();
        logger.info("Authentication for : " + userName);
        User user = userRepository.findByUserNameAndPassword(userName, password);
        if (user == null) {
            throw new BadCredentialsException("Invalid username/password");
        }
        return new UsernamePasswordAuthenticationToken(user, password);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(BizAuthentication.class);
    }

}
