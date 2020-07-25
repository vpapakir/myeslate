package gr.cti.eslate.database.engine;


public class JoinUnableToPerformException extends Exception {
    public String message;
    public JoinUnableToPerformException() {};
    public JoinUnableToPerformException(String msg) {
        super(msg);
        message = msg;
    }
}
