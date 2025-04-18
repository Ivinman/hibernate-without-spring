package ru.aston.dto;

import lombok.Data;

@Data
public class UserDtoIn {
    private final String name;
    private final String email;
    private final Integer age;
}
