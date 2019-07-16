package com.kamkry.app.controller;

import com.kamkry.app.model.AppUser;
import com.kamkry.app.service.UserService;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@CrossOrigin
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    SessionFactory sessionFactory;

    @GetMapping("/{id}")
    public AppUser getUser(@PathVariable(value = "id") Integer id) {
        return userService.get(id);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("")
    public List<AppUser> getAllUsers() {
        return userService.getAll();
    }

    @PostMapping("")
    public void saveUser(@RequestBody AppUser user) {
        userService.save(user);
    }

    @PutMapping("")
    public void updateUser(@RequestBody AppUser user) {
        userService.update(user);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Integer id) {
        userService.delete(id);
    }

    @GetMapping("/current")
    public @ResponseBody
    String currentUser() {
        return SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
    }
}
