package pl.rafiki.typer.exceptionhandling;

public enum ErrorCode {
    OTHER_ERROR,
    ARGUMENT_NOT_VALID,
    CANNOT_FINISH_MATCH_DUE_TO_NULL_SCORE,
    MATCH_DOES_NOT_EXIST,
    MATCH_IS_ALREADY_FINISHED,
    POINT_RULES_CODE_ALREADY_TAKEN,
    POINT_RULES_DOES_NOT_EXIST,
    INVALID_CREDENTIALS,
    INVALID_REFRESH_TOKEN,
    EXPIRED_REFRESH_TOKEN,
    TOURNAMENT_CODE_ALREADY_TAKEN,
    TOURNAMENT_DOES_NOT_EXIST,
    EMAIL_ADDRESS_ALREADY_TAKEN,
    INCORRECT_PASSWORD,
    INVALID_EMAIL,
    PASSWORD_DOES_NOT_MATCH_PATTERN,
    USER_DOES_NOT_EXIST,
    USERNAME_ALREADY_TAKEN,
    INVALID_TOKEN,
    NO_PERMISSION,
    BET_ALREADY_EXISTS,
    BET_DOES_NOT_EXIST,
    CANNOT_ADD_BECAUSE_MATCH_ALREADY_STARTED,
    CANNOT_UPDATE_BECAUSE_MATCH_ALREADY_STARTED
}
