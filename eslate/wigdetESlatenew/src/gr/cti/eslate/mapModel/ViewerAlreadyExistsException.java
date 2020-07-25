package gr.cti.eslate.mapModel;

public class ViewerAlreadyExistsException extends java.lang.Exception {
    public ViewerAlreadyExistsException() {
        super("Viewer removed from the map. It already exists in its list.");
    }
} 