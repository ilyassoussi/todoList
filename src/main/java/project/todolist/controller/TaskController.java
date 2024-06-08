package project.todolist.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import project.todolist.dto.TaskDTO;
import project.todolist.dto.TaskTransformer;
import project.todolist.model.Priority;
import project.todolist.model.Task;
import project.todolist.service.TaskService;
import project.todolist.service.ToDoService;

@Controller
@RequiredArgsConstructor
@RequestMapping("/tasks")
public class TaskController {
    private final TaskService taskService;
    private final ToDoService todoService;

    @PreAuthorize("hasAuthority('ADMIN') or " +
                  "principal.id==@toDoServiceImpl.readById(#todoId).owner.id or " +
                  "@toDoServiceImpl.readById(#todoId).collaborators" +
                  ".contains(@userServiceImpl.readById(principal.id))")
    @GetMapping("/create/todos/{todo_id}")
    public String create(@PathVariable("todo_id") long todoId, Model model) {
        model.addAttribute("task", new TaskDTO());
        model.addAttribute("todo", todoService.readById(todoId));
        model.addAttribute("priorities", Priority.values());
        return "task-create";
    }

    @PreAuthorize("hasAuthority('ADMIN') or " +
                  "principal.id==@toDoServiceImpl.readById(#todoId).owner.id or " +
                  "@toDoServiceImpl.readById(#todoId).collaborators" +
                  ".contains(@userServiceImpl.readById(principal.id))")
    @PostMapping("/create/todos/{todo_id}")
    public String create(@PathVariable("todo_id") long todoId, Model model,
                         @Validated @ModelAttribute("task") TaskDTO taskDTO, BindingResult result) {
        if (result.hasErrors()) {
            model.addAttribute("todo", todoService.readById(todoId));
            model.addAttribute("priorities", Priority.values());
            return "task-create";
        }
        Task task = TaskTransformer.convertDTOToEntity(
                taskDTO,
                todoService.readById(taskDTO.getToDoId()),
                taskService.getStateByName("New")
        );
        taskService.save(task);
        return "redirect:/todos/%d/tasks".formatted(todoId);
    }

    @PreAuthorize("hasAuthority('ADMIN') or " +
                  "principal.id==@toDoServiceImpl.readById(#todoId).owner.id or " +
                  "@toDoServiceImpl.readById(#todoId).collaborators" +
                  ".contains(@userServiceImpl.readById(principal.id))")
    @GetMapping("/{task_id}/update")
    public String update(@PathVariable("task_id") long taskId, Model model) {
        TaskDTO taskDTO = TaskTransformer.convertEntityToDTO(taskService.readById(taskId));
        model.addAttribute("task", taskDTO);
        model.addAttribute("toDoId", taskDTO.getToDoId());
        model.addAttribute("priorities", Priority.values());
        model.addAttribute("states", taskService.getAllStates());
        return "task-update";
    }

    @PreAuthorize("hasAuthority('ADMIN') or " +
                  "principal.id==@toDoServiceImpl.readById(#todoId).owner.id or " +
                  "@toDoServiceImpl.readById(#todoId).collaborators" +
                  ".contains(@userServiceImpl.readById(principal.id))")
    @PostMapping("/{task_id}/update")
    public String update(@PathVariable("task_id") long taskId, Model model, @Validated @ModelAttribute("task") TaskDTO taskDTO, BindingResult result) {
        if (result.hasErrors()) {
            model.addAttribute("priorities", Priority.values());
            model.addAttribute("states", taskService.getAllStates());
            return "task-update";
        }
        taskDTO.setId(taskId);
        Task task = TaskTransformer.convertDTOToEntity(
                taskDTO,
                todoService.readById(taskDTO.getToDoId()),
                taskService.getStateByName(taskDTO.getState())
        );
        taskService.save(task);
        return "redirect:/todos/%d/tasks".formatted(task.getToDo().getId());
    }

    @GetMapping("/{task_id}/read")
    public String read(@PathVariable("task_id") long taskId, Model model) {
        TaskDTO taskDTO = TaskTransformer.convertEntityToDTO(taskService.readById(taskId));
        model.addAttribute("task", taskDTO);
        return "task-info";
    }

    @PreAuthorize("hasAuthority('ADMIN') or " +
                  "principal.id==@toDoServiceImpl.readById(#todoId).owner.id or " +
                  "@toDoServiceImpl.readById(#todoId).collaborators" +
                  ".contains(@userServiceImpl.readById(principal.id))")
    @GetMapping("/{task_id}/delete/todos/{todo_id}")
    public String delete(@PathVariable("task_id") long taskId, @PathVariable("todo_id") long todoId) {
        taskService.delete(taskId);
        return "redirect:/todos/%d/tasks".formatted(todoId);
    }
}
