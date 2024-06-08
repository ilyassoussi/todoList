package project.todolist.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import project.todolist.exception.NullReferenceEntityException;
import project.todolist.model.State;
import project.todolist.model.Task;
import project.todolist.repository.StateRepository;
import project.todolist.repository.TaskRepository;
import project.todolist.service.TaskService;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {
    private final TaskRepository taskRepository;
    private final StateRepository stateRepository;

    @Override
    public Task save(Task task) {
        if (task == null) {
            log.error("TaskServiceImpl#save: Given Task cannot be saved (task=null)");
            throw new NullReferenceEntityException("Given ToDo cannot be saved (task=null)");
        }
        return taskRepository.save(task);
    }

    @Override
    public Task readById(long id) {
        return taskRepository.findById(id).orElseThrow(() -> {
            log.error("TaskServiceImpl#readById: Task (id=" + id + ") was not found");
            throw new EntityNotFoundException("Task (id=" + id + ") was not found");
        });
    }

    @Override
    public void delete(long id) {
        taskRepository.findById(id)
                .ifPresentOrElse(task -> {
                    log.info("delete(id): Deleted Task (id=" + id + ")");
                    taskRepository.delete(task);
                }, () -> {
                    log.error("TaskServiceImpl#delete: Task (id=" + id + ") was not found");
                    throw new EntityNotFoundException("Task (id=" + id + ") was not found");
                });
    }

    @Override
    public List<Task> getAllTaskOfToDo(long id) {
        return taskRepository.getByTodoId(id);
    }

    @Override
    public State createState(State state) {
        if (state == null) {
            log.error("TaskServiceImpl#createState: Given State cannot be null");
            throw new NullReferenceEntityException("Given State cannot be null");
        }
        return stateRepository.save(state);
    }

    @Override
    public State getStateByName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("State's name cannot be null or empty");
        }
        return stateRepository.getByName(name).orElseThrow(() -> {
            log.error("TaskServiceImpl#getStateByName: State (name=" + name + ") was not found");
            throw new EntityNotFoundException("State (name=" + name + ") was not found");
        });
    }

    @Override
    public void deleteStateByName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("State's name cannot be null or empty");
        }
        stateRepository.getByName(name)
                .ifPresentOrElse(state -> {
                    log.info("TaskServiceImpl#deleteStateByName: Deleted State (name=" + name + ")");
                    stateRepository.delete(state);
                }, () -> {
                    log.error("TaskServiceImpl#deleteStateByName: State (name=" + name + ") was not found");
                    throw new EntityNotFoundException("State (name=" + name + ") was not found");
                });
    }

    @Override
    public List<State> getAllStates() {
        return stateRepository.findAll();
    }
}
