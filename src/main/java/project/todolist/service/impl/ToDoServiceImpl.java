package project.todolist.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import project.todolist.exception.NullReferenceEntityException;
import project.todolist.exception.UserIsOwnerOfThisToDoException;
import project.todolist.model.ToDo;
import project.todolist.model.User;
import project.todolist.repository.ToDoRepository;
import project.todolist.repository.UserRepository;
import project.todolist.service.ToDoService;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ToDoServiceImpl implements ToDoService {
    private final ToDoRepository toDoRepository;
    private final UserRepository userRepository;

    @Override
    public ToDo save(ToDo toDo) {
        if (toDo == null) {
            log.error("ToDoServiceImpl#save: Given ToDo cannot be saved (toDo=null)");
            throw new NullReferenceEntityException("Given ToDo cannot be null");
        }
        return toDoRepository.save(toDo);
    }

    @Override
    public ToDo readById(long id) {
        return toDoRepository.findById(id).orElseThrow(() -> {
            log.error("ToDoServiceImpl#readById: ToDo (id=%d) was not found".formatted(id));
            throw new EntityNotFoundException("ToDo (id=%d) was not found".formatted(id));
        });
    }

    @Override
    public void delete(long id) {
        toDoRepository.findById(id)
                .ifPresentOrElse(toDo -> {
                            log.info("ToDoServiceImpl#delete: Deleted " + toDo);
                            toDoRepository.delete(toDo);
                        },
                        () -> {
                            log.error("ToDoServiceImpl#delete: ToDo (id=%d) was not found".formatted(id));
                            throw new EntityNotFoundException("ToDo (id=%d) was not found".formatted(id));
                        });
    }

    @Override
    public List<ToDo> getAll() {
        return toDoRepository.findAll();
    }

    @Override
    public List<ToDo> getAllToDoOfUser(long userId) {
        return toDoRepository.getToDosByUserId(userId);
    }

    @Override
    public void addCollaborator(Long toDoId, Long collaboratorId) {
        ToDo toDo = this.readById(toDoId);
        User user = getUserByIdAndCheckIsItOwnerOfToDo(toDo, collaboratorId);
        toDo.addCollaborator(user);
        toDoRepository.save(toDo);
        log.info("ToDoServiceImpl#addCollaborator: Added User (id=%d) as collaborator to ToDo (id=%s)".formatted(collaboratorId, toDo));
    }

    @Override
    public void removeCollaborator(Long toDoId, Long collaboratorId) {
        ToDo toDo = this.readById(toDoId);
        User user = getUserByIdAndCheckIsItOwnerOfToDo(toDo, collaboratorId);
        toDo.removeCollaborator(user);
        toDoRepository.save(toDo);
        log.info("ToDoServiceImpl#removeCollaborator: Removed User (id=%d) from collaborators of ToDo (id=%s)".formatted(collaboratorId, toDo));
    }

    private User getUserByIdAndCheckIsItOwnerOfToDo(ToDo toDo, long collaboratorId) {
        User user = userRepository.findById(collaboratorId).orElseThrow(() -> {
            log.error("ToDoServiceImpl#getUserByIdAndCheckIsItOwnerOfToDo: User (id=%d) was not found".formatted(collaboratorId));
            throw new EntityNotFoundException("User (id=%d) was not found".formatted(collaboratorId));
        });
        if (toDo.getOwner().equals(user)) {
            log.error("ToDoServiceImpl#getUserByIdAndCheckIsItOwnerOfToDo: This user is already an owner and cannot be added as collaborator");
            throw new UserIsOwnerOfThisToDoException("This user is already an owner and cannot be added as collaborator");
        }
        return user;
    }
}
