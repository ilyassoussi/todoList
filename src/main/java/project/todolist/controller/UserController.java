package project.todolist.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import project.todolist.model.User;
import project.todolist.service.UserService;

@Controller
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    @GetMapping("/create")
    public String create(Model model) {
        model.addAttribute("user", new User());
        return "user-create";
    }

    @PostMapping("/create")
    public String create(@Validated @ModelAttribute("user") User user, BindingResult result) {
        if (result.hasErrors()) {
            System.out.println(result);
            return "user-create";
        }
        user.setRole(userService.getRoleByName("USER"));
        User newUser = userService.create(user);
        return "redirect:/todos/all/users/%d".formatted(newUser.getId());
    }

    @PreAuthorize("hasAuthority('ADMIN') or #id==authentication.principal.id")
    @GetMapping("/{id}/read")
    public String read(@PathVariable long id, Model model) {
        User user = userService.readById(id);
        model.addAttribute("user", user);
        return "user-info";
    }

    @PreAuthorize("hasAuthority('ADMIN') or #id==authentication.principal.id")
    @GetMapping("/{id}/update")
    public String update(@PathVariable long id, Model model) {
        User user = userService.readById(id);
        model.addAttribute("user", user);
        model.addAttribute("roles", userService.getAllRoles());
        return "user-update";
    }

    @PreAuthorize("hasAuthority('ADMIN') or #id==authentication.principal.id")
    @PostMapping("/{id}/update")
    public String update(@PathVariable long id, Model model, @Validated @ModelAttribute("user") User user, BindingResult result) {
        User oldUser = userService.readById(id);
        if (result.hasErrors()) {
            user.setRole(oldUser.getRole());
            model.addAttribute("roles", userService.getAllRoles());
            return "user-update";
        }
        user.setRole(oldUser.getRole());
        userService.update(user);
        return "redirect:/users/%d/read".formatted(id);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/{id}/delete")
    public String delete(@PathVariable("id") long id) {
        userService.delete(id);
        return "redirect:/users/all";
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/all")
    public String getAll(Model model) {
        model.addAttribute("users", userService.getAll());
        return "users";
    }
}
