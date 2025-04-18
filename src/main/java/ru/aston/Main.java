package ru.aston;

import ru.aston.service.Service;
import ru.aston.service.ServiceImp;
import ru.aston.storage.UserStorage;
import ru.aston.storage.UserStorageImp;

public class Main {
    public static void main(String[] args) {
        UserStorage userStorage = new UserStorageImp();
        Service service = new ServiceImp(userStorage);
        UserInput userInput = new UserInput(service);
        userInput.command();
    }
}