package server.acode.global.common;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum ErrorCode {

    /* 400 BAD REQUEST */
    REQUEST_VALIDATION_EXCEPTION(HttpStatus.BAD_REQUEST, "잘못된 요청입니다."),
    USER_NOT_FOUND(HttpStatus.BAD_REQUEST, "해당 사용자를 찾을 수 없습니다."),
    FAMILY_NOT_FOUND(HttpStatus.BAD_REQUEST, "해당 계열을 찾을 수 없습니다."),
    INGREDIENT_NOT_FOUND(HttpStatus.BAD_REQUEST, "해당 향료를 찾을 수 없습니다."),
    INGREDIENT_TYPE_NOT_FOUND(HttpStatus.BAD_REQUEST, "해당 계열에 일치하는 계열타입을 찾을 수 없습니다."),
    BRAND_NOT_FOUND(HttpStatus.BAD_REQUEST,"해당 브랜드를 찾을 수 없습니다." ),
    SEARCH_NOT_FOUND(HttpStatus.BAD_REQUEST, "검색어가 필요합니다"),
    REVIEW_NOT_FOUND(HttpStatus.BAD_REQUEST, "해당 리뷰를 찾을 수 없습니다."),
    INVALID_AUTHENTICATION_CODE(HttpStatus.BAD_REQUEST, "유효한 인증 코드가 아닙니다."),


    /* 401 UNAUTHORIZED */
    EXPIRED_TOKEN_EXCEPTION(HttpStatus.UNAUTHORIZED, "만료된 토큰입니다."),
    TOKEN_VALIDATION_EXCEPTION(HttpStatus.UNAUTHORIZED, "유효하지 않은 토큰입니다."),


    /* 403 FORBIDDEN */
    NOT_ADMIN(HttpStatus.FORBIDDEN, "관리자가 아닙니다."),


    /* 404 NOT FOUND */
    FRAGRANCE_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 향수를 찾을 수 없습니다."),
    REVIEW_SEASON_NOT_FOUND(HttpStatus.NOT_FOUND, "관리자에게 문의 바랍니다."),
    REVIEW_LONGEVITY_NOT_FOUND(HttpStatus.NOT_FOUND, "관리자에게 문의 바랍니다."),
    REVIEW_INTENSITY_NOT_FOUND(HttpStatus.NOT_FOUND, "관리자에게 문의 바랍니다."),
    REVIEW_STYLE_NOT_FOUND(HttpStatus.NOT_FOUND, "관리자에게 문의 바랍니다."),
    IMAGE_NOT_FOUND(HttpStatus.NOT_FOUND, "관리자에게 문의 바랍니다"),


    /* 409 CONFLICT */
    UNLINK_FAIL(HttpStatus.CONFLICT, "UNLINK 도중 실패했습니다."),
    NICKNAME_ALREADY_USED(HttpStatus.CONFLICT, "이미 존재하는 닉네임입니다."),
    REVIEW_AUTHOR_MISMATCH(HttpStatus.CONFLICT, "리뷰 작성자와 일치하지 않는 사용자입니다."),
//    Review author mismatch


    /* 500 INTERNAL SERVER ERROR */
    JSON_PARSING_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "JSON parsing 에러입니다.");

    private final HttpStatus httpStatus;
    private final String message;

    public int getHttpStatusCode() {
        return httpStatus.value();
    }
}
