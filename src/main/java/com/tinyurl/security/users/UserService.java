package com.tinyurl.security.users;

import com.tinyurl.security.auth.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public User createUser(User user) throws Exception {
        User found = loadUserByUsername(user.getUserName());
        if (found != null) {
            throw new Exception("Username already exists");
        } else {
            return userRepository.save(user);
        }
    }

    public User getCurrentUser() {
        String userName = AuthService.currentAuditDetails().getUserName();
        return loadUserByUsername(userName);
    }

    public User loadUserByUsername(String username) {
        return userRepository.findByUserName(username);
    }
}
