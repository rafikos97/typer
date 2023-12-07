package pl.rafiki.typer.exceptionhandling;

public abstract class TyperException extends RuntimeException {
    public final ErrorCode errorCode;

    public TyperException(String message) {
        super(message);
        this.errorCode = getErrorCode();
    }

    protected abstract ErrorCode getErrorCode();
}