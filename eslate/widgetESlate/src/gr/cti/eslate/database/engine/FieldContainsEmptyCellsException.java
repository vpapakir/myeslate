package gr.cti.eslate.database.engine;


public class FieldContainsEmptyCellsException extends Exception {
    public String message;
    public FieldContainsEmptyCellsException() {};
    public FieldContainsEmptyCellsException(String msg) {
        super(msg);
        message = msg;
    }
}
