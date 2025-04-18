package ru.aston.service;

import ru.aston.dto.UserDtoOut;

import java.util.List;

public interface Service {
    boolean addUser(String userName, String userEmail, Integer userAge);
    UserDtoOut getUser(Integer userId);
    List<UserDtoOut> getUsers();
    boolean updateUser(Integer userId, String newUsername, String newUserEmail, Integer newUserAge);
    boolean deleteUser(Integer userId);
}