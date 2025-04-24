package ru.aston.service;

import ru.aston.dto.UserDtoIn;
import ru.aston.dto.UserDtoOut;

import java.util.List;

public interface Service {
    boolean addUser(UserDtoIn userDtoIn);
    UserDtoOut getUser(Integer userId);
    List<UserDtoOut> getUsers();
    boolean updateUser(Integer userId, UserDtoIn userDtoIn);
    boolean deleteUser(Integer userId);
}