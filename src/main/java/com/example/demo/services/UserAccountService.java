package com.example.demo.services;

import com.example.demo.models.User;
import com.example.demo.models.UserRole;
import com.example.demo.repositories.RoleDao;
import com.example.demo.repositories.UserDao;
import com.example.demo.repositories.UserRoleDao;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserAccountService {
    private final UserDao userDao;
    private final RoleDao roleDao;
    private final UserRoleDao userRoleDao;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;

    public User login(String username, String password) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        return userDao.findUserByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Invalid username or password"));
    }

    public User register(String firstName, String lastName, String email, String password) {
        if (userDao.findUserByEmail(email).isPresent()) {
            throw new IllegalArgumentException("Email already exists");
        }
        if (lastName == null || lastName.isEmpty()) {
            throw new IllegalArgumentException("Last name is empty");
        }
        if (firstName == null || firstName.isEmpty()) {
            throw new IllegalArgumentException("First name is empty");
        }
        if (password == null || password.isEmpty()) {
            throw new IllegalArgumentException("Password is empty");
        }
        log.info("Registering new user {}, {}", firstName, lastName);
        User user = new User();
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        return userDao.save(user);
    }

    public void setUserRoles(String username, String[] roles) {
        var user = userDao.findUserByEmail(username).orElseThrow(() -> new UsernameNotFoundException("Invalid username"));
        for (String roleName : roles) {
            var role = roleDao.findByName(roleName).orElseThrow(() -> new RuntimeException("Invalid role"));
            userRoleDao.save(new UserRole(user, role));
            log.info("Added user {} to role {}", username, role.getName());
        }
    }
}
