package gr.cti.eslate.database.engine;


public class IntersectionUnableToPerformException extends Exception {
    public String message;
    public IntersectionUnableToPerformException() {};
    public IntersectionUnableToPerformException(String msg) {
        super(msg);
        message = msg;
    }
}
