package project.todolist.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

import javax.persistence.EntityNotFoundException;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ModelAndView handleEntityNotFoundException(EntityNotFoundException exception) {
        return errorPageBuilder(HttpStatus.NOT_FOUND, exception);
    }

    @ExceptionHandler({NullReferenceEntityException.class, UserIsOwnerOfThisToDoException.class})
    @ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
    public ModelAndView handleNullReferenceEntityExceptionOrUserIsOwnerOfThisToDoException(RuntimeException exception) {
        return errorPageBuilder(HttpStatus.NOT_ACCEPTABLE, exception);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ModelAndView handleOtherExceptions(Exception exception) {
        return errorPageBuilder(HttpStatus.INTERNAL_SERVER_ERROR, exception);
    }

    private ModelAndView errorPageBuilder(HttpStatus status, Exception exception) {
        return new ModelAndView("error-page")
                .addObject("code", status.value())
                .addObject("codeDescription", status.getReasonPhrase())
                .addObject("message", exception.getMessage());
    }
}
