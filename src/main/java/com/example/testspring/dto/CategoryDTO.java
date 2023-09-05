package com.example.testspring.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class CategoryDTO {
    private Integer id;

    @NotBlank(message = "{not.blank}")
    private String name;
}
