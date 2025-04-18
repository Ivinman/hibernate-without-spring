package ru.aston.storage;

import ru.aston.model.User;

import java.util.List;

public interface UserStorage {
    void addUser(User user);
    User getUser(Integer userId);
    List<User> getUsers();
    void updateUser(Integer userId, User newUser);
    void deleteUser(Integer userId);
}
