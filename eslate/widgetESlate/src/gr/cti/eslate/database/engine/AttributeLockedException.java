package gr.cti.eslate.database.engine;


public class AttributeLockedException extends Exception {
    public AttributeLockedException() {};
    public AttributeLockedException(String msg) {
        super(msg);
    }
}
