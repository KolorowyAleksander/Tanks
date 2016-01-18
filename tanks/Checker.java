package tanks;

public abstract class Checker {
    protected double boundariesWidth;
    protected double boundariesHeight;

    public Checker(double width, double height) {
        this.boundariesWidth = width;
        this.boundariesHeight = height;
    }
}
