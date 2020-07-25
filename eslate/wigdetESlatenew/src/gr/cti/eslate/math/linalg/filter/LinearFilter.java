package gr.cti.eslate.math.linalg.filter;


public class LinearFilter implements AlphaFilter {
    
    public LinearFilter(){
    }
    
    public float filter(float alpha) {
        return alpha;
    }
}
