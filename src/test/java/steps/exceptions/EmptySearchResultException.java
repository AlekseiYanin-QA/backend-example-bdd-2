package steps.exceptions;

public class EmptySearchResultException extends SearchException {
    public EmptySearchResultException(String message) {
        super(message);
    }
}