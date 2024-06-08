package project.todolist.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.transaction.annotation.Transactional;
import project.todolist.exception.NullReferenceEntityException;
import project.todolist.model.State;
import project.todolist.model.Task;
import project.todolist.model.ToDo;
import project.todolist.repository.StateRepository;
import project.todolist.repository.TaskRepository;
import project.todolist.service.impl.TaskServiceImpl;

import javax.persistence.EntityNotFoundException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TaskServiceTests {
    @Mock
    private TaskRepository taskRepository;

    @Mock
    private StateRepository stateRepository;

    @InjectMocks
    private TaskServiceImpl taskService;

    private Task expectedTask;
    private State expectedState;
    private final long taskId = 1L;
    private final long todoId = 1L;
    private final String name = "name";

    @BeforeEach
    public void setUp() {
        expectedState = new State();
        expectedState.setName("name");
        expectedTask = new Task();
        expectedTask.setId(taskId);
        expectedTask.setName(name);
        expectedTask.setState(expectedState);
        ToDo toDo = new ToDo();
        toDo.setId(todoId);
        expectedTask.setToDo(toDo);
    }

    @Test
    @DisplayName("save(task) should throw NullReferenceEntityException when passing null")
    void saveNull() {
        assertThrows(NullReferenceEntityException.class, () -> taskService.save(null));
        verify(taskRepository, times(0)).save(any(Task.class));
    }

    @Test
    @Transactional
    @DisplayName("save(task) saves new correct Task")
    void saveNewNotNull() {
        when(taskRepository.save(any(Task.class))).thenReturn(expectedTask);

        var actual = taskService.save(expectedTask);

        verify(taskRepository).save(any(Task.class));
        assertEquals(expectedTask, actual);
        assertEquals(expectedTask.getId(), actual.getId());
    }

    @Test
    @Transactional
    @DisplayName("save(task) updates existing Task")
    void saveExistingNotNull() {
        when(taskRepository.save(any(Task.class))).thenReturn(expectedTask);

        var actual = taskService.save(expectedTask);

        verify(taskRepository).save(any(Task.class));
        assertEquals(expectedTask, actual);
        assertEquals(expectedTask.getId(), actual.getId());
    }

    @Test
    @DisplayName("readById(id) returns correct Task by id")
    void readByIdExisting() {
        when(taskRepository.findById(anyLong())).thenReturn(Optional.of(expectedTask));
        assertEquals(expectedTask, taskService.readById(anyLong()));
    }

    @Test
    @DisplayName("readById(id) throws an EntityNotFoundException when Task with given id was not found")
    void readByIdNonExisting() {
        assertThrows(EntityNotFoundException.class, () -> taskService.readById(1L));
    }

    @Test
    @Transactional
    @DisplayName("delete(id) removes existing Task")
    void deleteExisting() {
        when(taskRepository.findById(anyLong())).thenReturn(Optional.of(expectedTask));

        taskService.delete(taskId);

        verify(taskRepository).findById(anyLong());
        verify(taskRepository).delete(any(Task.class));
    }

    @Test
    @DisplayName("delete(id) throws an EntityNotFoundException when Task with given id was not found")
    void deleteNonExisting() {
        assertThrows(EntityNotFoundException.class, () -> taskService.delete(1L));
        verify(taskRepository, times(0)).delete(any(Task.class));
    }

    @Test
    @DisplayName("getAllTaskOfToDo(todoId) returns a List with tasks of existing ToDo")
    void getAllTasksOfExistingToDo() {
        when(taskRepository.getByTodoId(anyLong())).thenReturn(List.of(new Task(), new Task(), new Task()));

        var actual = taskService.getAllTaskOfToDo(todoId);
        assertEquals(3, actual.size());
        verify(taskRepository).getByTodoId(anyLong());
    }

    @Test
    @DisplayName("getAllTaskOfToDo(todoId) throws EntityNotFoundException when ToDo with given id was not found")
    void getAllTasksOfNonExistingToDo() {
        when(taskService.getAllTaskOfToDo(anyLong())).thenThrow(new EntityNotFoundException());
        assertThrows(EntityNotFoundException.class, () -> taskService.getAllTaskOfToDo(todoId));
    }

    @Test
    @DisplayName("getAllTaskOfToDo(todoId) returns an empty list when ToDo with that id has no created Task")
    void getEmptyListWhenExistingToDoHasNoTasks() {
        when(taskRepository.getByTodoId(anyLong())).thenReturn(Collections.emptyList());

        var actual = taskService.getAllTaskOfToDo(todoId);
        assertTrue(actual.isEmpty());
    }

    @Test
    @DisplayName("createNewState(state) throws NullReferenceEntityException when passing null")
    void createNullState() {
        assertThrows(NullReferenceEntityException.class, () -> taskService.createState(null));
        verify(stateRepository, times(0)).save(any(State.class));
    }

    @Test
    @Transactional
    @DisplayName("createNewState(state) saves correct new State")
    void createNonNullState() {
        when(stateRepository.save(any(State.class))).thenReturn(expectedState);

        var actual = taskService.createState(expectedState);
        verify(stateRepository).save(any(State.class));
        assertEquals(expectedState, actual);
        assertEquals(expectedState.getId(), actual.getId());
    }

    @Test
    @DisplayName("getStateByName(name) returns existing State")
    void getStateByNameExisting() {
        when(stateRepository.getByName(anyString())).thenReturn(Optional.of(expectedState));

        assertEquals(expectedState, taskService.getStateByName(name));
        verify(stateRepository).getByName(name);
    }

    @Test
    @DisplayName("getStateByName(name) throws EntityNotFoundException when State with that name was not found")
    void getStateByNameNonExisting() {
        assertThrows(EntityNotFoundException.class, () -> taskService.getStateByName(name));
    }

    @Test
    @DisplayName("getStateByName(name) throws IllegalArgumentException when passing null")
    void getStateByNullName() {
        assertThrows(IllegalArgumentException.class, () -> taskService.getStateByName(null));
    }

    @Test
    @DisplayName("deleteStateByName(name) removes existing State")
    void deleteExistingStateByName() {
        when(stateRepository.getByName(anyString())).thenReturn(Optional.of(expectedState));

        taskService.deleteStateByName(name);

        verify(stateRepository).getByName(anyString());
        verify(stateRepository).delete(any(State.class));
    }

    @Test
    @DisplayName("deleteStateByName(name) throws EntityNotFoundException when State with that name was not found")
    void deleteNonExistingStateByName() {
        assertThrows(EntityNotFoundException.class, () -> taskService.deleteStateByName(expectedState.getName()));
        verify(stateRepository, times(0)).delete(any(State.class));
    }

    @Test
    @DisplayName("deleteStateByName(name) throws IllegalArgumentException when passing null or empty string")
    void deleteStateByNullOrBlankName() {
        assertThrows(IllegalArgumentException.class, () -> taskService.deleteStateByName(null));
        assertThrows(IllegalArgumentException.class, () -> taskService.deleteStateByName(""));
        verify(stateRepository, times(0)).delete(any(State.class));
    }

    @Test
    @DisplayName("getAllStates() returns all existing states")
    void getAllStates() {
        when(stateRepository.findAll()).thenReturn(List.of(new State(), new State()));

        assertEquals(taskService.getAllStates(), stateRepository.findAll());
    }
}
