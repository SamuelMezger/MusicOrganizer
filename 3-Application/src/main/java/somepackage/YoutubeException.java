package somepackage;

public class YoutubeException extends Exception {

    private final String message;

    /**
     * Construct somepackage.YoutubeException with a message
     * @param message
     */
    public YoutubeException(String message) {
        this.message = message;
    }

    /**
     * Construct somepackage.YoutubeException from another exception
     * @param e Any exception
     */
    public YoutubeException(Exception e) {
        message = e.getMessage();
    }

    /**
     * Get exception message
     * @return exception message
     */
    @Override
    public String getMessage() {
            return message;
        }

}
