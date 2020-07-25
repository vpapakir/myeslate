package gr.cti.eslate.database.engine;


public class InvalidTitleException extends Exception {
    public InvalidTitleException() {};
    public InvalidTitleException(String msg) {
        super(msg);
    }
}
