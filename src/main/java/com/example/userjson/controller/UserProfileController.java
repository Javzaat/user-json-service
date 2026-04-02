package com.example.userjson.controller;

import com.example.userjson.model.UserProfile;
import com.example.userjson.service.UserProfileService;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping("/users")
public class UserProfileController {

    private final UserProfileService userProfileService;

    public UserProfileController(UserProfileService userProfileService) {
        this.userProfileService = userProfileService;
    }

    @PostMapping
    public UserProfile createUser(@RequestBody UserProfile user) {
        return userProfileService.createUser(user);
    }

    @GetMapping("/{id}")
    public UserProfile getUser(@PathVariable Long id) {
        return userProfileService.getUser(id);
    }

    @PutMapping("/{id}")
    public UserProfile updateUser(@PathVariable Long id, @RequestBody UserProfile user) {
        return userProfileService.updateUser(id, user);
    }

    @DeleteMapping("/{id}")
    public String deleteUser(@PathVariable Long id) {
        userProfileService.deleteUser(id);
        return "User deleted successfully";
    }

    @GetMapping
    public Collection<UserProfile> getAllUsers() {
        return userProfileService.getAllUsers();
    }
}