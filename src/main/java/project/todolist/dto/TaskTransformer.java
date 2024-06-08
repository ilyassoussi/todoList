package project.todolist.dto;

import project.todolist.model.Priority;
import project.todolist.model.State;
import project.todolist.model.Task;
import project.todolist.model.ToDo;

public class TaskTransformer {
    private TaskTransformer() {
    }

    public static Task convertDTOToEntity(TaskDTO taskDTO, ToDo toDo, State state) {
        Task task = new Task();
        task.setId(taskDTO.getId());
        task.setName(taskDTO.getName());
        task.setDescription(taskDTO.getDescription());
        task.setPriority(Priority.valueOf(taskDTO.getPriority()));
        task.setToDo(toDo);
        task.setState(state);
        return task;
    }

    public static TaskDTO convertEntityToDTO(Task task) {
        return new TaskDTO(
                task.getId(),
                task.getName(),
                task.getDescription(),
                task.getPriority().toString(),
                task.getToDo().getId(),
                task.getState().getName()
        );
    }
}
