package project.todolist.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import project.todolist.model.Task;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {
    @Query(value = "select * from tasks where todo_id = ?1", nativeQuery = true)
    List<Task> getByTodoId(long todoId);
}
