package project.todolist.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import project.todolist.exception.NullReferenceEntityException;
import project.todolist.model.Role;
import project.todolist.model.User;
import project.todolist.repository.RoleRepository;
import project.todolist.repository.UserRepository;
import project.todolist.service.impl.UserServiceImpl;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTests {
    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private UserServiceImpl userService;

    private User user;
    private Role role;

    @BeforeEach
    public void beforeEach() {
        user = new User();
        user.setId(1L);
        user.setFirstName("Mark");
        user.setLastName("Huang");
        user.setEmail("mark@gmail.com");
        user.setPassword("markPassword8");
        role = new Role();
        role.setName("NewRole");
        user.setRole(role);
    }

    @Test
    @DisplayName("create(user) saves new User")
    void createUser() {
        when(userRepository.save(any(User.class))).thenReturn(user);

        User actual = userService.create(user);
        assertEquals(actual, user);
        verify(userRepository).save(any(User.class));
    }

    @Test
    @DisplayName("create(user) throws an NullReferenceEntityException when given User is null")
    void createInvalidUser() {
        assertThrows(NullReferenceEntityException.class, () -> userService.create(null));
    }

    @Test
    @DisplayName("readById(id) returns existing User")
    void readByIdUser() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));

        User actual = userService.readById(user.getId());
        assertEquals(user, actual);
        verify(userRepository).findById(any(long.class));
    }

    @Test
    @DisplayName("readById(id) throws an EntityNotFoundException when User with given id was not found")
    void readByInvalidIdUser() {
        when(userRepository.findById(any(long.class))).thenReturn(Optional.empty());

        long notFoundId = 10L;
        assertThrows(EntityNotFoundException.class, () -> userService.readById(notFoundId));
        verify(userRepository).findById(any(long.class));
    }

    @Test
    @DisplayName("update(user) updates existing User")
    void updateExistingUser() {
        when(userRepository.findById(any(long.class))).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);

        assertEquals(user, userService.update(user));

        verify(userRepository).save(any(User.class));
        verify(userRepository).findById(any(long.class));
    }

    @Test
    @DisplayName("update(user) throws an EntityNotFoundException when User with given id was not found")
    void updateNonExistingUser() {
        assertThrows(EntityNotFoundException.class, () -> userService.update(user));
        verify(userRepository, times(0)).save(any(User.class));
    }

    @Test
    @DisplayName("update(user) throws an NullReferenceEntityException when given User is null")
    void updateNullUser() {
        assertThrows(NullReferenceEntityException.class, () -> userService.update(null));
        verify(userRepository, times(0)).save(any(User.class));
    }

    @Test
    @DisplayName("delete(id) removes existing User")
    void deleteExistingUser() {
        when(userRepository.findById(any(long.class))).thenReturn(Optional.of(user));

        userService.delete(user.getId());
        verify(userRepository).findById(any(long.class));
    }

    @Test
    @DisplayName("delete(id) throws an EntityNotFoundException when User with given id was not found")
    void deleteInvalidUser() {
        when(userRepository.findById(any(long.class))).thenReturn(Optional.empty());

        long notFoundId = 10L;
        assertThrows(EntityNotFoundException.class, () -> userService.delete(notFoundId));
        verify(userRepository).findById(any(long.class));
    }

    @Test
    @DisplayName("getAll() returns a list with all User")
    void getAllUsers() {
        var expected = new ArrayList<User>();

        when(userRepository.findAll()).thenReturn(expected);

        var actual = userService.getAll();
        assertEquals(actual, expected);

        expected.add(user);
        actual = userService.getAll();
        assertEquals(actual, expected);
        verify(userRepository, times(2)).findAll();
    }

    @Test
    @DisplayName("getRoleByName(name) returns existing Role")
    void getExistingRoleByName() {
        when(roleRepository.getByName(anyString())).thenReturn(Optional.of(role));
        assertEquals(role, userService.getRoleByName(role.getName()));
    }

    @Test
    @DisplayName("getRoleByName(name) throws an EntityNotFoundException when Role with given name was not found")
    void getNonExistingRoleByName() {
        assertThrows(EntityNotFoundException.class, () -> userService.getRoleByName("nonexistingrole"));
    }

    @Test
    @DisplayName("getRoleByName(name) throws an IllegalArgumentException when given name is null or blank")
    void getRoleByNullOrBlank() {
        assertThrows(IllegalArgumentException.class, () -> userService.getRoleByName(null));
        assertThrows(IllegalArgumentException.class, () -> userService.getRoleByName(""));
        verify(roleRepository, times(0)).getByName(any(String.class));
    }

    @Test
    @DisplayName("getAllRoles() returns a list with all Role")
    void getAllRoles() {
        var expected = new ArrayList<Role>();

        when(roleRepository.findAll()).thenReturn(expected);

        var actual = userService.getAllRoles();
        assertEquals(actual, expected);

        expected.add(role);
        actual = userService.getAllRoles();
        assertEquals(actual, expected);
        verify(roleRepository, times(2)).findAll();
    }
}
