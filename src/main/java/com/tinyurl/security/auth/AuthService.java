package com.tinyurl.security.auth;

import com.tinyurl.core.data.AuditDetails;
import com.tinyurl.security.jwt.JwtSubject;
import com.tinyurl.security.jwt.JwtUtil;
import com.tinyurl.security.users.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class AuthService {

    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private AuthManager authManager;

    public AuthResponse generateToken(AuthRequest authRequest) throws BadCredentialsException {
        try {
            Authentication authentication = authManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequest.getUserName(), authRequest.getPassword())
            );
            User user = (User) authentication.getPrincipal();
            return authenticationSuccess(user);
        } catch (BadCredentialsException ex) {
            throw new BadCredentialsException(ex.getMessage());
        }
    }

    private AuthResponse authenticationSuccess(User user) {
//        Set<String> grantedAuthorities = new HashSet<>();
        Set<GrantedAuthority> galist = new HashSet<>();
        // load authorities here
//        String authorities = grantedAuthorities.isEmpty() ? "" : String.join(",", grantedAuthorities);
        String token = jwtUtil.generateToken(user.getUserName());
        BizAuthentication auth = new BizAuthentication();
        auth.setToken(token);
        auth.setUserName(user.getUserName());
        AuthResponse authResponse = AuthResponse.builder().auth(auth).firstName(user.getFirstName()).lastName(user.getLastName()).build();
        recreateAuthentication(authResponse.getAuth(), token, galist);
        return authResponse;
    }

    public Authentication recreateAuthentication(BizAuthentication auth, String token, Set<GrantedAuthority> galist) {
        auth.setAuthorities(galist);
        auth.setToken(token);
        auth.setAuthenticated(true);
        SecurityContextHolder.getContext().setAuthentication(auth);
        return auth;
    }

    public static AuditDetails currentAuditDetails() {
        Authentication possible = SecurityContextHolder.getContext().getAuthentication();
        if (possible != null && possible instanceof BizAuthentication) {
           BizAuthentication auth = (BizAuthentication) possible;
            return new AuditDetails(auth.getUserName());
        }
        return new AuditDetails("SYSTEM");
    }
}
