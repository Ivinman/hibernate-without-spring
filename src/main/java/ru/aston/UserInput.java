//Не до конца разобрался как правильно использовать консольный ввод в тестах, поэтому тесты только для Service
//Если возможно подскажите пожалуйста
package ru.aston;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.aston.service.Service;

import java.util.Scanner;

@Slf4j
@RequiredArgsConstructor
public class UserInput {
    private final Scanner scan = new Scanner(System.in);
    private final Service service;

    public void startMenu() {
        while (true) {
            System.out.println("Выбереите команду:");
            System.out.println("1 - Добавить пользователя");
            System.out.println("2 - Получить информацию о пользователе");
            System.out.println("3 - Получить список пользователей");
            System.out.println("4 - Обновить данные пользователя");
            System.out.println("5 - Удалить пользователя");
            System.out.println("0 - Выход");
            String command = scan.nextLine();
            switch (command){
                case ("1"):
                    addUser();
                    continue;
                case ("2"):
                    getUser();
                    continue;
                case ("3"):
                    service.getUsers();
                    continue;
                case ("4"):
                    updateUser();
                    continue;
                case ("5"):
                    deleteUser();
                    continue;
                case ("0"):
                    break;
                default:
                    System.out.println("Такой команды нет");
                    log.info("Некорректный ввод команды");
                    continue;
            }
            break;
        }
    }

    private void addUser() {
        String name = getUserNameInput();
        if (name == null) {
            return;
        }
        String email = getUserEmailInput();
        if (email == null) {
            return;
        }
        Integer age = getUserAgeInput();
        if (age == null) {
            return;
        }
        while (!service.addUser(name, email, age)) {
            if (!secondChoice()) {
                return;
            } else {
                email = getUserEmailInput();
                if (email == null) {
                    return;
                }
            }
        }
    }

    private void getUser() {
        Integer userId;
        while (true) {
            userId = getUserIdInput();
            if (userId == null) {
                return;
            }
            if (service.getUser(userId) == null) {
                if (!secondChoice()) {
                    return;
                }
            } else {
                break;
            }
        }
    }

    private void updateUser() {
        Integer userIdDb;
        while (true) {
            userIdDb = getUserIdInput();
            if (userIdDb == null) {
                return;
            }
            if (service.getUser(userIdDb) == null) {
                if (!secondChoice()) {
                    return;
                }
            } else {
                break;
            }
        }
        String newName = getUserNameInput();
        if (newName == null) {
            return;
        }
        String newEmail = getUserEmailInput();
        if (newEmail == null) {
            return;
        }
        Integer newAge = getUserAgeInput();
        if (newAge == null) {
            return;
        }
        while (!service.updateUser(userIdDb, newName, newEmail, newAge)) {
            if (!secondChoice()) {
                return;
            } else {
                newEmail = getUserEmailInput();
                if (newEmail == null) {
                    return;
                }
            }
        }
    }

    private void deleteUser() {
        Integer userIdDel;
        while (true) {
            userIdDel = getUserIdInput();
            if (userIdDel == null) {
                return;
            }
            if (!service.deleteUser(userIdDel)) {
                if (!secondChoice()) {
                    return;
                }
            } else {
                break;
            }
        }
    }

    private String getUserNameInput() {
        String name;
        while (true) {
            System.out.println("Введите имя пользователя: ");
            name = scan.nextLine();
            if (name.contains(" ") || name.chars().anyMatch(Character::isDigit)) {
                System.out.println("Неккоректный ввод. Поле не должно быть пустым, содержать цифры или пробелы.");
                log.info("Ошибка валидации при вводе имени");
                if (!secondChoice()) {
                    return null;
                }
                continue;
            }
            break;
        }
        return name;
    }

    private String getUserEmailInput() {
        String email;
        while (true) {
            System.out.println("Введите email пользователя: ");
            email = scan.nextLine();
            if (email.contains(" ") || !email.contains("@")) {
                System.out.println("Неккоректный ввод. Поле не должно быть пустым, " +
                        "содержать пробелы или не иметь знака '@'.");
                log.info("Ошибка валидации при вводе почты");
                if (!secondChoice()) {
                    return null;
                }
                continue;
            }
            break;
        }
        return email;
    }

    private Integer getUserAgeInput() {
        String userAgeStr;
        Integer userAge;
        while (true) {
            try {
                System.out.println("Введите возраст пользователя: ");
                userAgeStr = scan.nextLine();
                userAge = Integer.parseInt(userAgeStr);
                break;
            } catch (Exception e) {
                System.out.println("Некорректный ввод возраста");
                log.info("Неверный формат при вводе возраста");
                if (!secondChoice()) {
                    return null;
                }
            }
        }
        return userAge;
    }

    public Integer getUserIdInput() {
        Integer userId;
        while (true) {
            try {
                System.out.println("Введите id пользователя");
                userId = Integer.parseInt(scan.nextLine());
                break;
            } catch (NumberFormatException e) {
                System.out.println("Некорректный ввод id пользователя");
                log.info("Неверный формат при вводе id пользователя");
                if (!secondChoice()) {
                    return null;
                }
            }
        }
        return userId;
    }

    private boolean secondChoice() {
        while (true) {
            System.out.println("Чтобы попробовать ещё раз введите 1. Для возврата в меню введите 0");
            String userConfComm = scan.nextLine();
            switch (userConfComm) {
                case ("1"):
                    return true;
                case ("0"):
                    return false;
                default:
                    System.out.println("Такой команды нет");
                    log.info("Некорректная команда при подвыборе");
            }
        }
    }
}
