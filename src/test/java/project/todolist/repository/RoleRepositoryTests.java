package project.todolist.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@Sql(scripts = {"/schema.sql", "/data.sql"})
public class RoleRepositoryTests {
    @Autowired
    private RoleRepository roleRepository;

    @Test
    @DisplayName("getByName(name) fetches Role by its name")
    void getByNameFound() {
        assertTrue(roleRepository.getByName("ADMIN").isPresent());
    }

    @Test
    @DisplayName("getByName(name) returns an empty Optional<Role> if such name doesn't exist")
    void getByNameNotFound() {
        assertTrue(roleRepository.getByName("RANDOMROLE").isEmpty());
    }
}
