package ru.aston;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.PostgreSQLContainer;
import ru.aston.dto.UserDtoIn;
import ru.aston.dto.UserDtoOut;
import ru.aston.model.User;
import ru.aston.service.Service;
import ru.aston.service.ServiceImp;
import ru.aston.storage.UserStorage;
import ru.aston.storage.UserStorageImp;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
public class ServiceDaoTest {
    private static final PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:latest");
    private static final Properties properties = new Properties();
    private final UserStorage userStorage = new UserStorageImp(properties);
    private final Service service = new ServiceImp(userStorage);

    private final UserDtoIn goodUserDtoIn1 = new UserDtoIn("Test_name", "Test@email", 23);
    private final UserDtoIn goodUserDtoIn2= new UserDtoIn("Test_NAME", "Test@EMAIL", 30);
    private final UserDtoIn sameEmailUser= new UserDtoIn("TestName", "Test@email", 23);

    @BeforeAll
    public static void setUp() throws Exception {
        try {
            postgreSQLContainer.start();
            InputStream inputStream = new FileInputStream("src\\test\\resources\\hibernateTest.properties");
            properties.load(inputStream);
            properties.setProperty("hibernate.connection.url", postgreSQLContainer.getJdbcUrl());
            properties.setProperty("hibernate.connection.username", postgreSQLContainer.getUsername());
            properties.setProperty("hibernate.connection.password", postgreSQLContainer.getPassword());
        } catch (FileNotFoundException e) {
            log.info("Файл по пути \"src\\test\\resources\\hibernateTest.properties\" не был найден");
        }
    }

    @AfterAll
    public static void closeContainer() {
        postgreSQLContainer.close();
    }

    @BeforeEach
    public void addFirstUser() {
        service.addUser(goodUserDtoIn1.getName(), goodUserDtoIn1.getEmail(), goodUserDtoIn1.getAge());
    }

    @Test
    public void addUser() {
        assertEquals(1, userStorage.getUsers().size());

        service.addUser(goodUserDtoIn2.getName(), goodUserDtoIn2.getEmail(), goodUserDtoIn2.getAge());
        User firstUser = userStorage.getUser(1);
        User secondUser = userStorage.getUser(2);
        assertEquals(firstUser.getName(), goodUserDtoIn1.getName());
        assertEquals(firstUser.getEmail(), goodUserDtoIn1.getEmail());
        assertEquals(firstUser.getAge(), goodUserDtoIn1.getAge());
        assertEquals(secondUser.getName(), goodUserDtoIn2.getName());
        assertEquals(secondUser.getEmail(), goodUserDtoIn2.getEmail());
        assertEquals(secondUser.getAge(), goodUserDtoIn2.getAge());
    }

    @Test
    public void getUser() {
        assertNull(service.getUser(2));

        UserDtoOut userDtoOut = service.getUser(1);
        assertEquals(goodUserDtoIn1.getName(), userDtoOut.getName());
        assertEquals(goodUserDtoIn1.getEmail(), userDtoOut.getEmail());
        assertEquals(goodUserDtoIn1.getAge(), userDtoOut.getAge());
        assertNotNull(userDtoOut.getCreated_at());
    }

    @Test
    public void getUsers() {
        service.addUser(goodUserDtoIn2.getName(), goodUserDtoIn2.getEmail(), goodUserDtoIn2.getAge());
        assertEquals(2, userStorage.getUsers().size());
    }

    @Test
    public void updateUser() {
        assertFalse(service.updateUser(2, goodUserDtoIn2.getName(), goodUserDtoIn2.getEmail(), goodUserDtoIn2.getAge()));
        assertFalse(service.updateUser(1, sameEmailUser.getName(), sameEmailUser.getEmail(), sameEmailUser.getAge()));

        String creationDate = service.getUser(1).getCreated_at();
        service.updateUser(1, goodUserDtoIn2.getName(), goodUserDtoIn2.getEmail(), goodUserDtoIn2.getAge());
        UserDtoOut userDtoOut = service.getUser(1);
        assertEquals(goodUserDtoIn2.getName(), userDtoOut.getName());
        assertEquals(goodUserDtoIn2.getEmail(), userDtoOut.getEmail());
        assertEquals(goodUserDtoIn2.getAge(), userDtoOut.getAge());
        assertEquals(creationDate, userDtoOut.getCreated_at());
    }

    @Test
    public void deleteUser() {
        assertFalse(service.deleteUser(2));

        assertEquals(1, userStorage.getUsers().size());
        service.deleteUser(1);
        assertEquals(0, userStorage.getUsers().size());
    }
}