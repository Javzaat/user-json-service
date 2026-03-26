package com.example.userjson.model;

import lombok.Data;

@Data
public class UserProfile {
    private Long id;
    private String name;
    private String email;
    private String bio;
    private String phone;
    private String imageUrl;
}