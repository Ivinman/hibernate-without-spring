package ru.aston.dto;

import lombok.Data;

@Data
public class UserDtoOut {
    private final String name;
    private final String email;
    private final Integer age;
    private final String created_at;

    @Override
    public String toString() {
        return "Имя: " + name + "\n"
                + "Почта: " + email + "\n"
                + "Возраст: " + age + "\n"
                + "Дата создания: " + created_at +"\n";
    }
}
