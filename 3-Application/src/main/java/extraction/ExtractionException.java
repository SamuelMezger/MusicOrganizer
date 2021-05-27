package extraction;

public class ExtractionException extends Exception {

    public ExtractionException(final String message) {
        super(message);
    }

    public ExtractionException(final Throwable cause) {
        super(cause);
    }

    public ExtractionException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
