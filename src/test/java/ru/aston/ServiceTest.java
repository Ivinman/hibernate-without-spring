package ru.aston;

import jakarta.persistence.NoResultException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.aston.dto.UserDtoIn;
import ru.aston.dto.UserMapper;
import ru.aston.model.User;
import ru.aston.service.Service;
import ru.aston.service.ServiceImp;
import ru.aston.storage.UserStorage;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ServiceTest {
    private final UserStorage userStorage = mock();
    private final Service service = new ServiceImp(userStorage);

    private static final UserDtoIn goodUserDtoIn1 = new UserDtoIn("Test_name", 23);
    private static final UserDtoIn goodUserDtoIn2= new UserDtoIn("Test_NAME", 30);
    private static final UserDtoIn sameEmailUser= new UserDtoIn("TestName", 23);

    @BeforeAll
    public static void setUp() {
        goodUserDtoIn1.setEmail("Test@email");
        goodUserDtoIn2.setEmail("Test@EMAIL");
        sameEmailUser.setEmail("Test@email");
    }

    @Test
    public void addUser() {
        assertTrue(service.addUser(goodUserDtoIn1));

        when(userStorage.getUsers()).thenReturn(List.of(UserMapper.toUser(goodUserDtoIn1)));
        assertFalse(service.addUser(sameEmailUser));
    }

    @Test
    public void addBadUser() {
        when(userStorage.getUsers()).thenReturn(List.of(UserMapper.toUser(goodUserDtoIn1)));
        assertFalse(service.addUser(sameEmailUser));
    }

    @Test
    public void addMultUsers() {
        when(userStorage.getUsers()).thenReturn(List.of(UserMapper.toUser(goodUserDtoIn1)));
        assertTrue(service.addUser(goodUserDtoIn2));
    }

    @Test
    public void getUser() {
        User user = UserMapper.toUser(goodUserDtoIn1);
        when(userStorage.getUser(1)).thenReturn(user);
        when(userStorage.getUser(2)).thenThrow(new NoResultException());
        assertEquals(UserMapper.toUserDtoOut(user), service.getUser(1));
        assertNull(service.getUser(2));
    }

    @Test
    public void getUsers() {
        when(userStorage.getUsers()).thenReturn(List.of(UserMapper.toUser(goodUserDtoIn1), UserMapper.toUser(goodUserDtoIn2)));
        assertEquals(2, service.getUsers().size());
    }

    @Test
    public void updateUser() {
        User user = UserMapper.toUser(goodUserDtoIn1);
        User updatedUser = UserMapper.toUser(goodUserDtoIn2);

        when(userStorage.getUsers()).thenReturn(List.of(user));
        assertFalse(service.addUser(sameEmailUser));

        when(userStorage.getUser(2)).thenThrow(new NoResultException());
        assertFalse(service.updateUser(2, goodUserDtoIn2));

        when(userStorage.getUser(1)).thenReturn(updatedUser);
        assertTrue(service.updateUser(1, goodUserDtoIn2));
    }

    @Test
    public void deleteUser() {
        assertTrue(service.deleteUser(1));

        doThrow(NoResultException.class).when(userStorage).deleteUser(2);
        assertFalse(service.deleteUser(2));
    }
}