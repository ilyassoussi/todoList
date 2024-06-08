package project.todolist.model;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
@ToString
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Pattern(regexp = "[A-Z][a-z]+",
            message = "Must start with a capital letter followed by one or more lowercase letters")
    @Column(name = "first_name", nullable = false)
    @Size(min = 2, max = 255, message = "First name must be between 2 and 255 characters")
    private String firstName;

    @Pattern(regexp = "[A-Z][a-z]+",
            message = "Must start with a capital letter followed by one or more lowercase letters")
    @Column(name = "last_name")
    @Size(min = 2, max = 255, message = "Last name must be between 2 and 255 characters")
    private String lastName;

    @Email
    @Column(name = "email", nullable = false, unique = true)
    @Size(min = 5, max = 255, message = "Email must be between 5 and 255 characters")
    private String email;

    @Column(name = "password", nullable = false)
    @Size(min = 6, max = 255, message = "Password must be between 6 and 255 characters")
    private String password;

    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;

    @OneToMany(mappedBy = "owner", cascade = CascadeType.REMOVE)
    @Setter(AccessLevel.PRIVATE)
    @ToString.Exclude
    private List<ToDo> myToDos = new ArrayList<>();

    @ManyToMany(mappedBy = "collaborators", fetch = FetchType.LAZY)
    @Setter(AccessLevel.PRIVATE)
    @ToString.Exclude
    private List<ToDo> collaborations = new ArrayList<>();
}
