package com.tinyurl.security.jwt;

import com.tinyurl.security.auth.AuthService;
import com.tinyurl.security.auth.BizAuthentication;
import com.tinyurl.security.users.User;
import com.tinyurl.security.users.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Component
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;
    @Lazy
    @Autowired
    private AuthService authService;
    @Autowired
    private UserService userService;
    private static final String TOKEN_HEADER = "Authorization";
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authorizationHeader = request.getHeader(TOKEN_HEADER);
        String token = null;
        String userName = null;
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            token = authorizationHeader.substring(7);
            userName = jwtUtil.extractUsername(token);
        }
        if (userName != null) {
            BizAuthentication auth = new BizAuthentication();
            auth.setUserName(userName);
            User user = userService.loadUserByUsername(Objects.requireNonNull(auth.getUserName()));
            if (ObjectUtils.isEmpty(user)) {
                logger.debug("USER DETAILS COULD NOT BE RETRIEVED...");
                filterChain.doFilter(request, response);
                return;
            }
            Set<GrantedAuthority> galist = new HashSet<>();
            // if using jwtSubject, get granted authorities from jwtSubject's 'authorities' String here
            authService.recreateAuthentication(auth, token, galist);
        } else {
            logger.debug("NO USERNAME RETRIEVED FROM TOKEN");
        }
        filterChain.doFilter(request, response);
    }
}
