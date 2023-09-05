package com.example.testspring.client.dto.dto;

import lombok.Data;

import java.util.List;

@Data
public class LoginUserDTO {
    private int id;
    private String name;
    private String username;
    private List<Authority> authorities;

    @Data
    public static class Authority {
        private String authority;
    }
}
