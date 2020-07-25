package gr.cti.eslate.math.linalg.filter;


public class SinFilter implements AlphaFilter {
    public float filter(float alpha) {
        return (float) (0.5f * (Math.sin(Math.PI * (alpha - 0.5f)) + 1));
    }
}
