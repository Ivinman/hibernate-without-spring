package ru.aston;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.aston.dto.UserDtoIn;
import ru.aston.service.Service;

import java.util.Scanner;

@Slf4j
@RequiredArgsConstructor
public class UserInput {
    private final Service service;

    public void startMenu(Scanner scan) {
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
                    addUser(scan);
                    continue;
                case ("2"):
                    getUser(scan);
                    continue;
                case ("3"):
                    service.getUsers();
                    continue;
                case ("4"):
                    updateUser(scan);
                    continue;
                case ("5"):
                    deleteUser(scan);
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

    private void addUser(Scanner scanner) {
        UserDtoIn userDtoIn = getUserDtoIn(scanner);
        if (userDtoIn == null) {
            return;
        }
        while (!service.addUser(userDtoIn)) {
            if (!secondChoice(scanner)) {
                return;
            } else {
                String email = getUserEmailInput(scanner);
                if (email == null) {
                    return;
                }
                userDtoIn.setEmail(email);
            }
        }
    }

    private void getUser(Scanner scanner) {
        Integer userId;
        while (true) {
            userId = getUserIdInput(scanner);
            if (userId == null) {
                return;
            }
            if (service.getUser(userId) == null) {
                if (!secondChoice(scanner)) {
                    return;
                }
            } else {
                break;
            }
        }
    }

    private void updateUser(Scanner scanner) {
        Integer userIdDb;
        while (true) {
            userIdDb = getUserIdInput(scanner);
            if (userIdDb == null) {
                return;
            }
            if (service.getUser(userIdDb) == null) {
                if (!secondChoice(scanner)) {
                    return;
                }
            } else {
                break;
            }
        }
        UserDtoIn newUserDtoIn = getUserDtoIn(scanner);
        if (newUserDtoIn == null) {
            return;
        }
        while (!service.updateUser(userIdDb, newUserDtoIn)) {
            if (!secondChoice(scanner)) {
                return;
            } else {
                String newEmail = getUserEmailInput(scanner);
                if (newEmail == null) {
                    return;
                }
                newUserDtoIn.setEmail(newEmail);
            }
        }
    }

    private void deleteUser(Scanner scanner) {
        Integer userIdDel;
        while (true) {
            userIdDel = getUserIdInput(scanner);
            if (userIdDel == null) {
                return;
            }
            if (!service.deleteUser(userIdDel)) {
                if (!secondChoice(scanner)) {
                    return;
                }
            } else {
                break;
            }
        }
    }

    private String getUserNameInput(Scanner scanner) {
        String name;
        while (true) {
            System.out.println("Введите имя пользователя: ");
            name = scanner.nextLine();
            if (name.contains(" ") || name.chars().anyMatch(Character::isDigit)) {
                System.out.println("Неккоректный ввод. Поле не должно быть пустым, содержать цифры или пробелы.");
                log.info("Ошибка валидации при вводе имени");
                if (!secondChoice(scanner)) {
                    return null;
                }
                continue;
            }
            break;
        }
        return name;
    }

    private String getUserEmailInput(Scanner scanner) {
        String email;
        while (true) {
            System.out.println("Введите email пользователя: ");
            email = scanner.nextLine();
            if (email.contains(" ") || !email.contains("@")) {
                System.out.println("Неккоректный ввод. Поле не должно быть пустым, " +
                        "содержать пробелы или не иметь знака '@'.");
                log.info("Ошибка валидации при вводе почты");
                if (!secondChoice(scanner)) {
                    return null;
                }
                continue;
            }
            break;
        }
        return email;
    }

    private Integer getUserAgeInput(Scanner scanner) {
        String userAgeStr;
        Integer userAge;
        while (true) {
            try {
                System.out.println("Введите возраст пользователя: ");
                userAgeStr = scanner.nextLine();
                userAge = Integer.parseInt(userAgeStr);
                break;
            } catch (Exception e) {
                System.out.println("Некорректный ввод возраста");
                log.info("Неверный формат при вводе возраста");
                if (!secondChoice(scanner)) {
                    return null;
                }
            }
        }
        return userAge;
    }

    public Integer getUserIdInput(Scanner scanner) {
        Integer userId;
        while (true) {
            try {
                System.out.println("Введите id пользователя");
                userId = Integer.parseInt(scanner.nextLine());
                break;
            } catch (NumberFormatException e) {
                System.out.println("Некорректный ввод id пользователя");
                log.info("Неверный формат при вводе id пользователя");
                if (!secondChoice(scanner)) {
                    return null;
                }
            }
        }
        return userId;
    }

    private boolean secondChoice(Scanner scanner) {
        while (true) {
            System.out.println("Чтобы попробовать ещё раз введите 1. Для возврата в меню введите 0");
            String userConfComm = scanner.nextLine();
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

    private UserDtoIn getUserDtoIn(Scanner scanner) {
        String name = getUserNameInput(scanner);
        if (name == null) {
            return null;
        }
        String email = getUserEmailInput(scanner);
        if (email == null) {
            return null;
        }
        Integer age = getUserAgeInput(scanner);
        if (age == null) {
            return null;
        }
        UserDtoIn userDtoIn = new UserDtoIn(name, age);
        userDtoIn.setEmail(email);
        return userDtoIn;
    }
    
//    private Integer get() {
//
//    }
}
