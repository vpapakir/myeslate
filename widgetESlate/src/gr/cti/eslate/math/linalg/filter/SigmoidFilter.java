package gr.cti.eslate.math.linalg.filter;


/** Takes in an alpha between 0 and 1 and outputs a sigmoid also
 between 0 and 1. */

public class SigmoidFilter implements AlphaFilter {
    private float weight;
    private int range;
    private static final float DEFAULT = 3.0f;

    public SigmoidFilter() {
        this(DEFAULT);
    }

    public SigmoidFilter(float weight) {
        this.weight = weight;
    }

    public float weight() {
        return weight;
    }

    public void  setWeight(float weight) {
        this.weight = weight;
    }
    

    public float filter(float alpha) {
        // The factor of 10 gives the sigmoid better range. Maybe should
        // not put this in implicitly.
        return (float) (1.0f / (1.0f + Math.exp(-weight * 20 * (alpha - 0.5f))));
    }
    
}
