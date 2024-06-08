package project.todolist.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskDTO {
    private long id;

    @NotBlank(message = "The 'name' cannot be empty")
    private String name;

    private String description;

    @NotNull
    private String priority;

    @NotNull
    private long toDoId;

    private String state;
}
