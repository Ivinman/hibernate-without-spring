package ru.aston;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.aston.dto.UserDtoIn;
import ru.aston.dto.UserDtoOut;
import ru.aston.model.User;
import ru.aston.service.Service;
import ru.aston.service.ServiceImp;
import ru.aston.storage.UserStorage;
import ru.aston.storage.UserStorageImp;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
class ServiceTest {
    private final UserStorage userStorage = new UserStorageImp();
    private final Service service = new ServiceImp(userStorage);
    private UserDtoIn goodUserDtoIn1;
    private UserDtoIn goodUserDtoIn2;
    private UserDtoIn sameEmailUser;

    @BeforeEach
    public void setGoodUserDtoIn1() {
        goodUserDtoIn1 = new UserDtoIn("Test_name",
                "Test@email",
                23);
    }

    @BeforeEach
    public void setGoodUserDtoIn2() {
        goodUserDtoIn2 = new UserDtoIn("Test_NAME",
                "Test@EMAIL",
                30);
    }

    @BeforeEach
    public void setSameEmailUserDto() {
        sameEmailUser = new UserDtoIn("TestName",
                "Test@email",
                23);
    }

    @Test
    public void addUser() {
        service.addUser(goodUserDtoIn1.getName(), goodUserDtoIn1.getEmail(), goodUserDtoIn1.getAge());
        User user = userStorage.getUser(1);

        assertEquals(goodUserDtoIn1.getName(), user.getName());
        assertEquals(goodUserDtoIn1.getEmail(), user.getEmail());
        assertEquals(goodUserDtoIn1.getAge(), user.getAge());
    }

    @Test
    public void addBadUser() {
        service.addUser(goodUserDtoIn1.getName(), goodUserDtoIn1.getEmail(), goodUserDtoIn1.getAge());
        service.addUser(sameEmailUser.getName(), sameEmailUser.getEmail(), sameEmailUser.getAge());
        List<User> users = userStorage.getUsers();

        assertEquals(1, users.size());
    }

    @Test
    public void addMultUsers() {
        service.addUser(goodUserDtoIn1.getName(), goodUserDtoIn1.getEmail(), goodUserDtoIn1.getAge());
        service.addUser(goodUserDtoIn2.getName(), goodUserDtoIn2.getEmail(), goodUserDtoIn2.getAge());
        List<User> users = userStorage.getUsers();

        assertEquals(2, users.size());
        assertEquals(goodUserDtoIn1.getName(), users.getFirst().getName());
        assertEquals(goodUserDtoIn1.getEmail(), users.getFirst().getEmail());
        assertEquals(goodUserDtoIn1.getAge(), users.getFirst().getAge());
        assertEquals(goodUserDtoIn2.getName(), users.get(1).getName());
        assertEquals(goodUserDtoIn2.getEmail(), users.get(1).getEmail());
        assertEquals(goodUserDtoIn2.getAge(), users.get(1).getAge());
    }

    @Test
    public void getUser() {
        service.addUser(goodUserDtoIn1.getName(), goodUserDtoIn1.getEmail(), goodUserDtoIn1.getAge());
        UserDtoOut userDtoOut = service.getUser(1);
        UserDtoOut notAddedUser = service.getUser(2);

        assertNotNull(userDtoOut);
        assertNull(notAddedUser);
    }

    @Test
    public void getUsers() {
        service.addUser(goodUserDtoIn1.getName(), goodUserDtoIn1.getEmail(), goodUserDtoIn1.getAge());
        service.addUser(goodUserDtoIn2.getName(), goodUserDtoIn2.getEmail(), goodUserDtoIn2.getAge());
        List<UserDtoOut> users = service.getUsers();

        assertEquals(2, users.size());
    }

    @Test
    public void updateUser() {
        service.addUser(goodUserDtoIn1.getName(), goodUserDtoIn1.getEmail(), goodUserDtoIn1.getAge());
        service.updateUser(1, goodUserDtoIn2.getName(), goodUserDtoIn2.getEmail(), goodUserDtoIn2.getAge());

        assertEquals(goodUserDtoIn2.getName(), userStorage.getUser(1).getName());
        assertEquals(goodUserDtoIn2.getEmail(), userStorage.getUser(1).getEmail());
        assertEquals(goodUserDtoIn2.getAge(), userStorage.getUser(1).getAge());
    }

    @Test
    public void notUpdateUser() {
        service.addUser(goodUserDtoIn1.getName(), goodUserDtoIn1.getEmail(), goodUserDtoIn1.getAge());
        service.updateUser(2, goodUserDtoIn2.getName(), goodUserDtoIn2.getEmail(), goodUserDtoIn2.getAge());

        assertNotEquals(goodUserDtoIn2.getName(), userStorage.getUser(1).getName());

        service.addUser(sameEmailUser.getName(), sameEmailUser.getEmail(), sameEmailUser.getAge());

        assertNotEquals(sameEmailUser.getName(), userStorage.getUser(1).getName());
    }

    @Test
    public void deleteUser() {
        service.addUser(goodUserDtoIn1.getName(), goodUserDtoIn1.getEmail(), goodUserDtoIn1.getAge());
        service.deleteUser(2);

        assertNotNull(userStorage.getUser(1));

        service.deleteUser(1);

        assertEquals(0, userStorage.getUsers().size());
    }

}