package project.todolist.exception;

public class NullReferenceEntityException extends RuntimeException {
    public NullReferenceEntityException() {
        super();
    }

    public NullReferenceEntityException(String message) {
        super(message);
    }
}
