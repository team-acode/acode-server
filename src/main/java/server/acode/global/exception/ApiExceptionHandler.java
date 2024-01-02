package server.acode.global.exception;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import server.acode.global.common.ErrorResponse;

@RestControllerAdvice
public class ApiExceptionHandler{

    @ExceptionHandler(value = CustomException.class)
    public ResponseEntity<ErrorResponse> handleCustomException(CustomException e) {
        //TODO 에러에 대한 후처리

        System.out.println("[handleCustomException] {} : {}"+e.getErrorCode().name()+ e.getErrorCode().getMessage());
        return ErrorResponse.error(e);
    }

}
