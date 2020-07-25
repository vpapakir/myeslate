package gr.cti.eslate.mapModel;


class CouldntDeleteFeatureException extends Exception {
    CouldntDeleteFeatureException() {
        super("Couldn't delete node.");
    }    
}    