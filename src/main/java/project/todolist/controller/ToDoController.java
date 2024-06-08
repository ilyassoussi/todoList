package project.todolist.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import project.todolist.model.ToDo;
import project.todolist.service.TaskService;
import project.todolist.service.ToDoService;
import project.todolist.service.UserService;

import java.time.LocalDateTime;

@Controller
@RequiredArgsConstructor
@RequestMapping("/todos")
public class ToDoController {
    private final ToDoService todoService;
    private final TaskService taskService;
    private final UserService userService;

    @PreAuthorize("hasAuthority('ADMIN') or #ownerId==authentication.principal.id")
    @GetMapping("/create/users/{owner_id}")
    public String create(@PathVariable("owner_id") long ownerId, Model model) {
        model.addAttribute("todo", new ToDo());
        model.addAttribute("ownerId", ownerId);
        return "todo-create";
    }

    @PreAuthorize("hasAuthority('ADMIN') or #ownerId==authentication.principal.id")
    @PostMapping("/create/users/{owner_id}")
    public String create(@PathVariable("owner_id") long ownerId, @Validated @ModelAttribute("todo") ToDo toDo, BindingResult result) {
        if (result.hasErrors()) {
            return "todo-create";
        }
        toDo.setCreatedAt(LocalDateTime.now());
        toDo.setOwner(userService.readById(ownerId));
        todoService.save(toDo);
        return "redirect:/todos/all/users/" + ownerId;
    }

    @PreAuthorize("hasAuthority('ADMIN') or " +
                  "principal.id==@toDoServiceImpl.readById(#id).owner.id or " +
                  "@toDoServiceImpl.readById(#id).collaborators" +
                  ".contains(@userServiceImpl.readById(principal.id))")
    @GetMapping("/{id}/tasks")
    public String read(@PathVariable long id, Model model) {
        ToDo todo = todoService.readById(id);
        var tasks = taskService.getAllTaskOfToDo(id);
        var users = userService.getAll().stream()
                .filter(user -> user.getId() != todo.getOwner().getId())
                .toList();
        model.addAttribute("todo", todo);
        model.addAttribute("tasks", tasks);
        model.addAttribute("users", users);
        return "todo-tasks";
    }

    @GetMapping("/{todo_id}/update/users/{owner_id}")
    public String update(@PathVariable("todo_id") long todoId, @PathVariable("owner_id") long ownerId, Model model) {
        ToDo toDo = todoService.readById(todoId);
        model.addAttribute("todo", toDo);
        return "todo-update";
    }

    @PostMapping("/{todo_id}/update/users/{owner_id}")
    public String update(@PathVariable("todo_id") long todoId, @PathVariable("owner_id") long ownerId,
                         @Validated @ModelAttribute("todo") ToDo todo, BindingResult result) {
        if (result.hasErrors()) {
            todo.setOwner(userService.readById(ownerId));
            return "todo-update";
        }
        ToDo oldTodo = todoService.readById(todoId);
        todo.setOwner(oldTodo.getOwner());
        todo.setCollaborators(oldTodo.getCollaborators());
        todoService.save(todo);
        return "redirect:/todos/all/users/%d".formatted(ownerId);
    }

    @GetMapping("/{todo_id}/delete/users/{owner_id}")
    public String delete(@PathVariable("todo_id") long todoId, @PathVariable("owner_id") long ownerId) {
        todoService.delete(todoId);
        return "redirect:/todos/all/users/%d".formatted(ownerId);
    }

    @PreAuthorize("hasAuthority('ADMIN') or #userId==authentication.principal.id")
    @GetMapping("/all/users/{user_id}")
    public String getAll(@PathVariable("user_id") long userId, Model model) {
        var todos = todoService.getAllToDoOfUser(userId);
        model.addAttribute("todos", todos);
        model.addAttribute("user", userService.readById(userId));
        return "todos-user";
    }

    @PreAuthorize("hasAuthority('ADMIN') or authentication.principal.id==@toDoServiceImpl.readById(#id).owner.id")
    @GetMapping("/{id}/add")
    public String addCollaborator(@PathVariable long id, @RequestParam("user_id") long userId) {
        todoService.addCollaborator(id, userId);
        return "redirect:/todos/%d/tasks".formatted(id);
    }

    @PreAuthorize("hasAuthority('ADMIN') or authentication.principal.id==@toDoServiceImpl.readById(#id).owner.id")
    @GetMapping("/{id}/remove")
    public String removeCollaborator(@PathVariable long id, @RequestParam("user_id") long userId) {
        todoService.removeCollaborator(id, userId);
        return "redirect:/todos/%d/tasks".formatted(id);
    }
}
