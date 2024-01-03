package server.acode.global.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import server.acode.global.exception.CustomException;

import java.util.List;

@Getter
@Builder
@RequiredArgsConstructor
public class ErrorResponse {
    private final HttpStatus status;
    private final String code;
    private final String message;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)  // errors가 없다면 응답으로 내려가지 않도록 추가
    private final List<ValidationError> errors;

    /**
     * @Valid를 사용했을 때 에러가 발생한 경우
     * 어느 필드에서 에러가 발생했는지 응답을 위함
     */
    @Getter
    @Builder
    @RequiredArgsConstructor
    public static class ValidationError {

        private final String field;
        private final String message;

        public static ValidationError of(final FieldError fieldError) {
            return ValidationError.builder()
                    .field(fieldError.getField())
                    .message(fieldError.getDefaultMessage())
                    .build();
        }
    }

//    public ErrorResponse(ErrorCode errorCode) {
//        this.status = errorCode.getHttpStatus();
//        this.code = errorCode.name();
//        this.message = errorCode.getMessage();
//    }
//
//    public static ResponseEntity<ErrorResponse> error(CustomException e) {
//        return ResponseEntity
//                .status(e.getErrorCode().getHttpStatus())
//                .body(ErrorResponse.builder()
//                        .status(e.getErrorCode().getHttpStatus())
//                        .code(e.getErrorCode().name())
//                        .message(e.getErrorCode().getMessage())
//                        .build());
//    }
}
