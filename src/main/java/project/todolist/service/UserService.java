package project.todolist.service;

import project.todolist.model.Role;
import project.todolist.model.User;

import java.util.List;

public interface UserService {
    User create(User user);

    User update(User user);

    User readById(long id);

    void delete(long id);

    List<User> getAll();

    Role getRoleByName(String name);

    List<Role> getAllRoles();
}
