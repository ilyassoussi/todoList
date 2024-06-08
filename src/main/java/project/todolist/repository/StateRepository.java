package project.todolist.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import project.todolist.model.State;

import java.util.Optional;

public interface StateRepository extends JpaRepository<State, Long> {
    @Query(value = "select * from states where name = ?1", nativeQuery = true)
    Optional<State> getByName(String name);
}
