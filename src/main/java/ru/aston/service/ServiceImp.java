package ru.aston.service;

import jakarta.persistence.NoResultException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.aston.dto.UserDtoIn;
import ru.aston.dto.UserDtoOut;
import ru.aston.dto.UserMapper;
import ru.aston.storage.UserStorage;
import ru.aston.model.User;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class ServiceImp implements Service {
    private final UserStorage userStorage;

    @Override
    public boolean addUser(String userName, String userEmail, Integer userAge) {
        UserDtoIn userDtoIn = getUserDtoIn(userName, userEmail, userAge);
        if (userDtoIn == null) {
            return false;
        }
        User user = UserMapper.toUser(userDtoIn);
        userStorage.addUser(user);
        log.info("Пользователь добавлен");
        return true;
    }

    @Override
    public UserDtoOut getUser(Integer userId) {
        try {
            UserDtoOut userDtoOut = UserMapper.toUserDtoOut(userStorage.getUser(userId));
            System.out.println(UserMapper.toUserDtoOut(userStorage.getUser(userId)));
            return userDtoOut;
        } catch (NoResultException e) {
            System.out.println("Пользователя с данным id нет");
            return null;
        }
    }

    @Override
    public List<UserDtoOut> getUsers() {
        List<UserDtoOut> usersDtoOut = new ArrayList<>();
        for (User user : userStorage.getUsers()) {
            usersDtoOut.add(UserMapper.toUserDtoOut(user));
        }
        System.out.println(usersDtoOut);
        return usersDtoOut;
    }

    @Override
    public boolean updateUser(Integer userId, String newUserName, String newUserEmail, Integer newUserAge) {
        try {
            User user = userStorage.getUser(userId);
            UserDtoIn userDtoIn = getUserDtoIn(newUserName, newUserEmail, newUserAge);
            if (userDtoIn == null) {
                return false;
            }
            user.setName(userDtoIn.getName());
            user.setEmail(userDtoIn.getEmail());
            user.setAge(userDtoIn.getAge());
            userStorage.updateUser(userId, user);
            log.info("Данные пользователя с id = " + userId + " были обновлены");
            return true;
        } catch (NoResultException e) {
            System.out.println("Пользователя с данным id нет");
            return false;
        }
    }

    @Override
    public boolean deleteUser(Integer userId) {
        try {
            userStorage.getUser(userId);
            userStorage.deleteUser(userId);
            return true;
        } catch (NoResultException e) {
            System.out.println("Пользователя с данным id нет");
            return false;
        }
    }

    private UserDtoIn getUserDtoIn(String userName, String userEmail, Integer userAge) {
        for (User user : userStorage.getUsers()) {
            if (user.getEmail().equals(userEmail)) {
                System.out.println("Пользователь с такой почтой уже есть");
                log.info("Пользователь с такой почтой уже есть");
                return null;
            }
        }
        return new UserDtoIn(userName, userEmail, userAge);
    }
}
