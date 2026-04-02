package com.example.userjson.service;

import com.example.userjson.model.UserProfile;
import com.example.userjson.repository.UserProfileRepository;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class UserProfileService {

    private final UserProfileRepository userProfileRepository;

    public UserProfileService(UserProfileRepository userProfileRepository) {
        this.userProfileRepository = userProfileRepository;
    }

    public UserProfile createUser(UserProfile user) {
        return userProfileRepository.save(user);
    }

    public UserProfile getUser(Long id) {
        return userProfileRepository.findById(id).orElse(null);
    }

    public Collection<UserProfile> getAllUsers() {
        return userProfileRepository.findAll();
    }

    public UserProfile updateUser(Long id, UserProfile updatedUser) {
        UserProfile existingUser = userProfileRepository.findById(id).orElse(null);

        if (existingUser != null) {
            existingUser.setName(updatedUser.getName());
            existingUser.setEmail(updatedUser.getEmail());
            existingUser.setBio(updatedUser.getBio());
            existingUser.setPhone(updatedUser.getPhone());
            existingUser.setImageUrl(updatedUser.getImageUrl());
            return userProfileRepository.save(existingUser);
        }

        return null;
    }

    public void deleteUser(Long id) {
        userProfileRepository.deleteById(id);
    }
}