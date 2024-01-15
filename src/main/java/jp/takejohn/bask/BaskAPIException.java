package jp.takejohn.bask;

public class BaskAPIException extends RuntimeException {

    public BaskAPIException(String message) {
        super(message);
    }

    public BaskAPIException(Throwable cause) {
        super(cause);
    }

}
