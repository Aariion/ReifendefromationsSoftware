public class FlatGround implements GroundProfile {
    private final double y;

    public FlatGround(double y) {
        this.y = y;
    }

    @Override
    public double height(double x) {
        return y;
    }
}
