package ufsm.csi.cpo.exceptions;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ufsm.csi.cpo.modules.types.Response;

import java.util.Date;

@ControllerAdvice
public class ExceptionHandlerAdvice {
    @ExceptionHandler(PlatformAlreadyRegistered.class)
    public ResponseEntity<Response<String>> platformAlreadyRegistered(PlatformAlreadyRegistered e) {
       return new ResponseEntity<>(new Response<>(2000, e.getMessage(), new Date()), HttpStatus.PRECONDITION_FAILED);
    }

    @ExceptionHandler(NoMutualVersion.class)
    public ResponseEntity<Response<String>> noMutualVersion(NoMutualVersion e) {
        return new ResponseEntity<>(new Response<>(2000, e.getMessage(), new Date()), HttpStatus.PRECONDITION_FAILED);
    }

    @ExceptionHandler(JsonProcessingException.class)
    public ResponseEntity<Response<String>> platformAlreadyRegistered(JsonProcessingException e) {
        return new ResponseEntity<>(new Response<>(2000, e.getMessage(), new Date()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(PlatformNotRegistered.class)
    public ResponseEntity<Response<String>> platformNotRegistered(PlatformNotRegistered e) {
        return new ResponseEntity<>(new Response<>(2000, e.getMessage(), new Date()), HttpStatus.METHOD_NOT_ALLOWED);
    }
}
