package ru.aston;

import org.junit.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import ru.aston.service.Service;

import java.util.Scanner;

import static org.mockito.Mockito.*;

public class InputTest {
    private final Scanner scanner = mock();
    private final Service service = mock();
    private final UserInput userInput = new UserInput(service);

    @Test
    public void addUser() {
        when(scanner.nextLine()).thenReturn("1").thenReturn("name").thenReturn("@email").thenReturn("23").thenReturn("0");
        userInput.startMenu(scanner);
        verify(service, times(1)).addUser(anyString(), anyString(), anyInt());
    }

    @ParameterizedTest
    @CsvSource({"n ame, @email, 23",
            "name, email, 23",
            "name, @email, sd"})
    public void notAddUser(String name, String email, String age) {
        when(scanner.nextLine()).thenReturn("1").thenReturn(name).thenReturn(email).thenReturn(age).thenReturn("0");
        userInput.startMenu(scanner);
        verify(service, times(0)).addUser(anyString(), anyString(), anyInt());
    }

    @Test
    public void addUserSecondChoice() {
        when(scanner.nextLine()).thenReturn("1").thenReturn("n ame").thenReturn("1").thenReturn("name").
                thenReturn("@email").thenReturn("23").thenReturn("0");
        userInput.startMenu(scanner);
        verify(service, times(1)).addUser(anyString(), anyString(), anyInt());
    }

    @Test
    public void addSameEmailUser() {
        when(scanner.nextLine()).thenReturn("1").thenReturn("name").thenReturn("@email").thenReturn("23")
                .thenReturn("1").thenReturn("@email").thenReturn("0");
        when(service.addUser(anyString(), anyString(), anyInt())).thenReturn(false).thenReturn(true);
        userInput.startMenu(scanner);
        verify(service, times(2)).addUser(anyString(), anyString(), anyInt());
    }

    @Test
    public void getUser() {
        when(scanner.nextLine()).thenReturn("2").thenReturn("1")
                .thenReturn("2").thenReturn("2").thenReturn("0");
        userInput.startMenu(scanner);
        verify(service, times(1)).getUser(anyInt());
    }

    @Test
    public void getUserWrongId() {
        when(scanner.nextLine()).thenReturn("2").thenReturn("er").thenReturn("0").thenReturn("0");
        userInput.startMenu(scanner);
        verify(service, times(0)).getUser(anyInt());
    }
}
