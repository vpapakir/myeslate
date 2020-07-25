package gr.cti.eslate.math.linalg.filter;


public class ExpFilter implements AlphaFilter {
    private float rightOffset;

    public ExpFilter(float rightOffset) {
        this.rightOffset = rightOffset;
    }

    public float filter(float alpha) {
        return (float) (Math.exp(alpha)-rightOffset);
    }
}