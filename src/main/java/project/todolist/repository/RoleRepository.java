package project.todolist.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import project.todolist.model.Role;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    @Query(value = "select * from roles where name = ?1", nativeQuery = true)
    Optional<Role> getByName(String name);
}
