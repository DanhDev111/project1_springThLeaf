package com.example.testspring.dto;

import lombok.Data;

import java.util.List;

@Data
public class LoginUserDTO {
    private int id;
    private String name;
    private String username;
    private List<Authotiry> authorities;

    @Data
    public static class Authotiry {
        private String authority;
    }
}
