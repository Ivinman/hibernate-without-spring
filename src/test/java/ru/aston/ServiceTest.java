package ru.aston;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.aston.dto.UserDtoIn;
import ru.aston.dto.UserMapper;
import ru.aston.model.User;
import ru.aston.service.Service;
import ru.aston.service.ServiceImp;
import ru.aston.storage.UserStorage;
import ru.aston.storage.UserStorageImp;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;


@Transactional
class ServiceTest {
    private final UserStorage userStorage = new UserStorageImp();
    private final Service service = new ServiceImp(userStorage);
    private UserDtoIn userDtoIn1;
    private UserDtoIn userDtoIn2;

    @BeforeEach
    public void setUserDto1() {
        String name = "Test_name";
        String email = "Test@email";
        Integer age = 25;
        userDtoIn1 = new UserDtoIn(name, email, age);
    }

    @BeforeEach
    public void setUserDto2() {
        String name = "Test_name2";
        String email = "Test@email2";
        Integer age = 35;
        userDtoIn2 = new UserDtoIn(name, email, age);
    }

    @Test
    public void addUser() {
        User user = UserMapper.toUser(userDtoIn1);
        userStorage.addUser(user);
        User userFromDb = userStorage.getUser(1);

        assertThat(userFromDb.getId(), notNullValue());
        assertThat(userFromDb.getName(), equalTo(user.getName()));
        assertThat(userFromDb.getEmail(), equalTo(user.getEmail()));
        assertThat(userFromDb.getAge(), equalTo(user.getAge()));
        assertThat(userFromDb.getCreated_at(), notNullValue());

        userStorage.addUser(UserMapper.toUser(userDtoIn2));

        assertThat(userStorage.getUsers().size(), equalTo(2));
    }

    @Test
    public void addUserWithError() {

    }

}