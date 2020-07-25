package gr.cti.eslate.math.linalg.filter;


public class PowerFilter implements AlphaFilter {
    private float pow;

    public PowerFilter(float pow) {
        this.pow = pow;
    }

    public float filter(float alpha) {
        return (float) Math.pow(alpha, 1.0f / pow);
    }
}
