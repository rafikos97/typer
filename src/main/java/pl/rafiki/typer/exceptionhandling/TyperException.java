package pl.rafiki.typer.exceptionhandling;

public abstract class TyperException extends RuntimeException {
    public final String ERROR_CODE;

    public TyperException(String message) {
        super(message);
        this.ERROR_CODE = getErrorCode();
    }

    protected abstract String getErrorCode();
}