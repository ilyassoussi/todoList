package project.todolist.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@Sql(scripts = {"/schema.sql", "/data.sql"})
public class TaskRepositoryTests {
    @Autowired
    private TaskRepository taskRepository;

    @Test
    @DisplayName("getByToDoId(id) fetches a list with tasks from one ToDo by its id")
    public void getTasksByToDoTest() {
        taskRepository.getByTodoId(7L).forEach(t -> {
            assertEquals(7, t.getToDo().getId());
            assertEquals("Mike's To-Do #1", t.getToDo().getTitle());
        });
    }
}
