package project.todolist.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@Sql(scripts = {"/schema.sql", "/data.sql"})
public class StateRepositoryTests {
    @Autowired
    private StateRepository stateRepository;

    @Test
    @DisplayName("getByName(name) fetches State by its name")
    void getByNameFound() {
        assertTrue(stateRepository.getByName("New").isPresent());
    }

    @Test
    @DisplayName("getByName(name) returns an empty Optional<State> if such name doesn't exist")
    void getByNameNotFound() {
        assertTrue(stateRepository.getByName("Randomstate").isEmpty());
    }
}
