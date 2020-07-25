package gr.cti.eslate.base.container;



public class PageInfo {
    float topMargin = 2f;
    float bottomMargin = 2f;
    float leftMargin = 3f;
    float rightMargin = 3f;
    boolean fitToPage = false;
    boolean centerOnPage = false;
    int scale = 100;

    public PageInfo() {
    }

    public void setFitToPage(boolean fit) {
        fitToPage = fit;
    }

    public boolean isFitToPage() {
        return fitToPage;
    }

    public void setCenterOnPage(boolean c) {
        centerOnPage = c;
    }

    public boolean isCenterOnPage() {
        return centerOnPage;
    }

    public void setScale(int i) {
        if (scale >=1 && scale <= 100)
            scale = i;
    }

    public int getScale() {
        return scale;
    }

    public void setTopMargin(float top) {
        this.topMargin = top;
    }
    public float getTopMargin() {
        return topMargin;
    }

    public void setBottomMargin(float bottom) {
        this.bottomMargin = bottom;
    }
    public float getBottomMargin() {
        return bottomMargin;
    }

    public void setLeftMargin(float left) {
        this.leftMargin = left;
    }
    public float getLeftMargin() {
        return leftMargin;
    }

    public void setRightMargin(float right) {
        this.rightMargin = right;
    }
    public float getRightMargin() {
        return rightMargin;
    }
}