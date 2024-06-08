package project.todolist.exception;

public class UserIsOwnerOfThisToDoException extends RuntimeException {
    public UserIsOwnerOfThisToDoException(String message) {
        super(message);
    }
}
