package gr.cti.eslate.math.linalg;


public class Vec2i {
    private int x;
    private int y;

    public Vec2i(int x, int y) {
        this.x = x;
        this.y = y;
    }
    
    public void set(int x, int y){
        this.x = x;
        this.y = y;
    }

    public int x() {
        return x;
    }

    public int y() {
        return y;
    }

    public String toString() {
        return "x: " + x + " y: " + y;
    }
}