package doree.devg.service;

import doree.devg.entity.User;

import java.util.List;

public interface IUserService {
    List<User> getAllUsers();

    User getUserById(Long userId);

    User createUser(User user);

    User updateUser(Long userId, User user);

    void deleteUser(Long userId);
}
