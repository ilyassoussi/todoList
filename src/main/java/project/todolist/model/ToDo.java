package project.todolist.model;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "todos")
@Getter
@Setter
@EqualsAndHashCode(of = "id")
@NoArgsConstructor
@ToString
public class ToDo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "title", nullable = false, unique = true)
    @Size(min = 1, max = 255, message = "ToDo's title must be between 1 and 255 characters")
    private String title;

    @Column(name = "description", nullable = false)
    @Size(max = 255, message = "ToDo's description must be lesser than 255 characters")
    private String description;

    @Column(name = "created_at", nullable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private User owner;

    @OneToMany(mappedBy = "toDo", cascade = CascadeType.REMOVE)
    @Setter(AccessLevel.PRIVATE)
    @ToString.Exclude
    private List<Task> tasks = new ArrayList<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "todos_collaborators",
            joinColumns = @JoinColumn(name = "todo_id"),
            inverseJoinColumns = @JoinColumn(name = "collaborator_id"))
    @ToString.Exclude
    private List<User> collaborators = new ArrayList<>();

    public void addCollaborator(User collaborator) {
        collaborators.add(collaborator);
        collaborator.getCollaborations().add(this);
    }

    public void removeCollaborator(User collaborator) {
        collaborators.remove(collaborator);
        collaborator.getCollaborations().remove(this);
    }

}
