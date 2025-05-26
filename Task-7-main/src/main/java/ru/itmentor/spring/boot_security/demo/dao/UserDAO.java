package ru.itmentor.spring.boot_security.demo.dao;

import ru.itmentor.spring.boot_security.demo.model.User;

import java.util.List;

public interface UserDAO {
    void saveUser(User user);

    void updateUser(User user);

    void deleteUser(Long id);

    User findById(Long id);

    List<User> findAllUsers();

}
