package com.tinyurl.security.users;

import com.tinyurl.core.data.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "sec_user")
public class User extends BaseEntity {
    @Column(name="first_name", nullable = false)
    private String firstName;

    @Column(name="last_name", nullable = false)
    private String lastName;

    @Column(name="user_name", nullable = false, unique = true)
    private String userName;

    @Column(name="password", nullable = false)
    private String password;
}
