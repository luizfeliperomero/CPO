package ufsm.csi.cpo.exceptions;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionHandlerAdvice {
    @ExceptionHandler(PlatformAlreadyRegistered.class)
    public ResponseEntity<String> platformAlreadyRegistered(PlatformAlreadyRegistered e) {
       return new ResponseEntity<>(e.getMessage(), HttpStatus.METHOD_NOT_ALLOWED);
    }

    @ExceptionHandler(NoMutualVersion.class)
    public ResponseEntity<String> platformAlreadyRegistered(NoMutualVersion e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.PRECONDITION_FAILED);
    }

    @ExceptionHandler(JsonProcessingException.class)
    public ResponseEntity<String> platformAlreadyRegistered(JsonProcessingException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(PlatformNotRegistered.class)
    public ResponseEntity<String> platformNotRegistered(PlatformNotRegistered e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.PRECONDITION_FAILED);
    }
}
