package ru.aston.dto;

import ru.aston.model.User;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.format.DateTimeFormatter;

public class UserMapper {
    public static User toUser(UserDtoIn userDtoIn) {
        User user = new User();
        user.setName(userDtoIn.getName());
        user.setEmail(userDtoIn.getEmail());
        user.setAge(userDtoIn.getAge());
        user.setCreated_at(Timestamp.from(Instant.now()));
        return user;
    }

    public static UserDtoOut toUserDtoOut(User user) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy HH:mm");
        return new UserDtoOut(user.getName(),
                user.getEmail(),
                user.getAge(),
                formatter.format(user.getCreated_at().toLocalDateTime()));
    }
}
