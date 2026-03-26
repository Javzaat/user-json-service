package com.example.userjson.service;

import com.example.userjson.model.UserProfile;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Service
public class UserProfileService {

    private final Map<Long, UserProfile> users = new HashMap<>();
    private Long currentId = 1L;

    public UserProfile createUser(UserProfile user) {
        user.setId(currentId++);
        users.put(user.getId(), user);
        return user;
    }

    public UserProfile getUser(Long id) {
        return users.get(id);
    }

    public Collection<UserProfile> getAllUsers() {
        return users.values();
    }

    public UserProfile updateUser(Long id, UserProfile updatedUser) {
        UserProfile existingUser = users.get(id);

        if (existingUser != null) {
            existingUser.setName(updatedUser.getName());
            existingUser.setEmail(updatedUser.getEmail());
            existingUser.setBio(updatedUser.getBio());
            existingUser.setPhone(updatedUser.getPhone());
            existingUser.setImageUrl(updatedUser.getImageUrl()); // энэ мөр чухал
        }

        return existingUser;
    }

    public void deleteUser(Long id) {
        users.remove(id);
    }
}