/*
 * Created on 31 Ìבת 2006
 *
 */
package gr.cti.eslate.object3D.loaders.obj;

/**
 * Exception used to indicate that the loader encountered
 * a problem parsing the specified file.
 */
public class ParsingErrorException extends RuntimeException {

    public ParsingErrorException() {
        super();
    }

    public ParsingErrorException(String s) {
        super(s);
    }
}

