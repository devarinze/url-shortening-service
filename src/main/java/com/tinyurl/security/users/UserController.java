package com.tinyurl.security.users;

import com.tinyurl.core.data.Response;
import com.tinyurl.security.auth.AuthRequest;
import com.tinyurl.security.auth.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/v1/user")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private AuthService authService;

    @PostMapping("/signup")
    public Response signup(@RequestBody User user) throws Exception {
        User created = userService.createUser(user);
        AuthRequest authRequest = new AuthRequest(created.getUserName(), created.getPassword());
        return Response.of(authService.generateToken(authRequest));
    }

    @PostMapping("/login")
    public Response login(@RequestBody AuthRequest authRequest) throws Exception {
        return Response.of(authService.generateToken(authRequest));
    }

    @GetMapping("/me")
    public Response getCurrentUser() {
        return Response.of(userService.getCurrentUser());
    }
}
