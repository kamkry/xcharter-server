package com.kamkry.app.domain.user;

import com.kamkry.app.domain.user.exception.UserAlreadyExistsException;
import com.kamkry.app.domain.user.exception.UserNotFoundException;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@CrossOrigin
@PreAuthorize("hasRole('ROLE_ADMIN')")
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    SessionFactory sessionFactory;

    @Autowired
    PasswordEncoder passwordEncoder;

    @GetMapping("/{id}")
    public User getUser(@PathVariable(value = "id") Integer id) {
        if (userService.get(id) == null) throw new UserNotFoundException(id);
        return userService.get(id);
    }

    @GetMapping("")
    public List<User> getAllUsers() {

        return userService.getAll();
    }

    @PostMapping("")
    public void saveUser(@RequestBody User user) {
        if (userService.get(user.getUsername()) != null) {
            throw new UserAlreadyExistsException(user.getUsername());
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userService.save(user);
    }

    @PutMapping("")
    public void updateUser(@RequestBody User user) {
        userService.update(user);
    }

    @DeleteMapping("/{id}")
    public void disableUser(@PathVariable Integer id) {
        userService.disable(userService.get(id));
    }

    @GetMapping("/current")
    public @ResponseBody
    String currentUser() {
        return SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
    }


}
