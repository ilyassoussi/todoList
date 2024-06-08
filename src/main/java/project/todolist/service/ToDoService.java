package project.todolist.service;

import project.todolist.model.ToDo;

import java.util.List;

public interface ToDoService {
    ToDo save(ToDo toDo);

    ToDo readById(long id);

    void delete(long id);

    List<ToDo> getAll();

    List<ToDo> getAllToDoOfUser(long userId);

    void addCollaborator(Long toDoId, Long collaboratorId);

    void removeCollaborator(Long toDoId, Long collaboratorId);
}
