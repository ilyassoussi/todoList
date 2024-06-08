package project.todolist.service;

import project.todolist.model.State;
import project.todolist.model.Task;

import java.util.List;

public interface TaskService {
    Task save(Task task);

    Task readById(long id);

    void delete(long id);

    List<Task> getAllTaskOfToDo(long id);

    State createState(State state);

    State getStateByName(String name);

    void deleteStateByName(String name);

    List<State> getAllStates();

}
