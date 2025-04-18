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

    public void command() {
        outerloop:
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
                    String name = getUserNameInput();
                    if (name == null) {
                        continue;
                    }
                    String email = getUserEmailInput();
                    if (email == null) {
                        continue;
                    }
                    Integer age = getUserAgeInput();
                    if (age == null) {
                        continue;
                    }
                    while (!service.addUser(name, email, age)) {
                        if (!secondChoice(getUserConfInput())) {
                            continue outerloop;
                        } else {
                            email = getUserEmailInput();
                            if (email == null) {
                                continue outerloop;
                            }
                        }
                    }
                    continue;
                case ("2"):
                    Integer userId;
                    while (true) {
                        userId = getUserIdInput();
                        if (userId == null) {
                            continue outerloop;
                        }
                        if (service.getUser(userId) == null) {
                            if (!secondChoice(getUserConfInput())) {
                                continue outerloop;
                            }
                        } else {
                            break;
                        }
                    }
                    continue;
                case ("3"):
                    service.getUsers();
                    continue;
                case ("4"):
                    Integer userIdDb;
                    while (true) {
                        userIdDb = getUserIdInput();
                        if (userIdDb == null) {
                            continue outerloop;
                        }
                        if (service.getUser(userIdDb) == null) {
                            if (!secondChoice(getUserConfInput())) {
                                continue outerloop;
                            }
                        }
                        break;
                    }
                    String newName = getUserNameInput();
                    if (newName == null) {
                        continue;
                    }
                    String newEmail = getUserEmailInput();
                    if (newEmail == null) {
                        continue;
                    }
                    Integer newAge = getUserAgeInput();
                    if (newAge == null) {
                        continue;
                    }
                    while (!service.updateUser(userIdDb, newName, newEmail, newAge)) {
                        if (!secondChoice(getUserConfInput())) {
                            continue outerloop;
                        } else {
                            newEmail = getUserEmailInput();
                            if (newEmail == null) {
                                continue outerloop;
                            }
                        }
                    }
                    continue;
                case ("5"):
                    Integer userIdDel;
                    while (true) {
                        userIdDel = getUserIdInput();
                        if (userIdDel == null) {
                            continue outerloop;
                        }
                        if (!service.deleteUser(userIdDel)) {
                            if (!secondChoice(getUserConfInput())) {
                                continue outerloop;
                            }
                        }
                        break;
                    }
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

    private String getUserNameInput() {
        String name;
        while (true) {
            System.out.println("Введите имя пользователя: ");
            name = scan.nextLine();
            if (name.contains(" ") || name.chars().anyMatch(Character::isDigit)) {
                System.out.println("Неккоректный ввод. Поле не должно быть пустым, содержать цифры или пробелы.");
                log.info("Ошибка валидации при вводе имени");
                if (!secondChoice(getUserConfInput())) {
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
                if (!secondChoice(getUserConfInput())) {
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
                if (!secondChoice(getUserConfInput())) {
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
                if (!secondChoice(getUserConfInput())) {
                    return null;
                }
            }
        }
        return userId;
    }

    private String getUserConfInput() {
        System.out.println("Чтобы попробовать ещё раз введите 1. Для возврата в меню введите 0");
        return scan.nextLine();
    }

    private boolean secondChoice(String userConfComm) {
        while (true) {
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
