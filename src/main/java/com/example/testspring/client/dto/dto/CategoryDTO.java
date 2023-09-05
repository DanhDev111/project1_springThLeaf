package com.example.testspring.client.dto.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class CategoryDTO {
    private int id;

    @NotBlank(message = "{not.blank}")
    private String name;
}
