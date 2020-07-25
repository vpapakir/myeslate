package gr.cti.eslate.math.linalg.filter;


/** Applies a function with domain [0, 1] and range [0, 1] */

public interface AlphaFilter {

    /** Receives a value between 0.0f and 1.0f and returns a value
     between 0.0f and 1.0f */
    public float filter(float alpha);
}
