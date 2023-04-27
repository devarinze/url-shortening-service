package com.tinyurl.security.users;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUserName(String username);
    User findByUserNameAndPassword(String username, String password);
}
