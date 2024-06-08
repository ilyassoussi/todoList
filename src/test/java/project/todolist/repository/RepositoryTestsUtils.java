package project.todolist.repository;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import project.todolist.model.User;

import javax.persistence.EntityNotFoundException;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
class RepositoryTestsUtils {
    private TestEntityManager entityManager;
    private RoleRepository roleRepository;

    User persistedNewUser() {
        User user = new User();
        user.setEmail("user@gmail.com");
        user.setFirstName("First");
        user.setLastName("Last");
        user.setPassword("pAssw0rd");
        roleRepository.findById(1L).ifPresentOrElse(user::setRole, () -> {
            throw new EntityNotFoundException("Table roles is empty. Impossible to create User entity");
        });
        return entityManager.persist(user);
    }

    static RepositoryTestsUtils newInstance(TestEntityManager entityManager, RoleRepository repository) {
        return new RepositoryTestsUtils(entityManager, repository);
    }
}
