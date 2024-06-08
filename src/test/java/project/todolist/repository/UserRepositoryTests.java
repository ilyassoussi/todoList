package project.todolist.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.jdbc.Sql;
import project.todolist.model.User;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@Sql(scripts = {"/schema.sql", "/data.sql"})
public class UserRepositoryTests {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private TestEntityManager entityManager;

    @Test
    @DisplayName("findUserByEmail(email) saves an entity and fetches non empty Optional<User> by email")
    void foundByEmail() {
        RepositoryTestsUtils.newInstance(entityManager, roleRepository).persistedNewUser();
        String email = "user@gmail.com";
        Optional<User> userByEmail = userRepository.findUserByEmail(email);
        assertTrue(userByEmail.isPresent());
        assertEquals(userByEmail.get().getEmail(), email);
    }

    @Test
    @DisplayName("findUserByEmail(email) returns an empty Optional<User> when has not found user with such email")
    void notFoundByEmail() {
        assertTrue(userRepository.findUserByEmail("dummy@neverexistedemailserver.com").isEmpty());
    }
}
