package project.todolist.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.jdbc.Sql;
import project.todolist.model.ToDo;
import project.todolist.model.User;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@Sql(scripts = {"/schema.sql", "/data.sql"})
public class ToDoRepositoryTests {
    @Autowired
    private ToDoRepository toDoRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    @DisplayName("getToDosByUserId(id) fetches a non empty list with ToDo by owner id")
    public void getToDosByUserIdFound() {
        ToDo toDo1 = new ToDo();
        toDo1.setTitle("title1");
        toDo1.setCreatedAt(LocalDateTime.now().minusMonths(1));

        ToDo toDo2 = new ToDo();
        toDo2.setTitle("title2");
        toDo2.setCreatedAt(LocalDateTime.now().minusDays(1));

        User user = RepositoryTestsUtils.newInstance(entityManager, roleRepository).persistedNewUser();

        toDo1.setOwner(user);
        toDo2.setOwner(user);

        entityManager.persist(toDo1);
        entityManager.persist(toDo2);

        List<ToDo> expected = List.of(toDo1, toDo2);
        List<ToDo> actual = toDoRepository.getToDosByUserId(user.getId());
        assertEquals(expected.size(), actual.size());
    }

    @Test
    @DisplayName("getToDosByUserId(id) returns an empty list with ToDo by non-existing id")
    void getToDosByUserIdNotFound() {
        assertTrue(toDoRepository.getToDosByUserId(20L).isEmpty());
    }
}
