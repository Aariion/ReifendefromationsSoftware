public class GaussianBumpGround implements GroundProfile {
    private final double baseY;
    private final double centerX;
    private final double amplitude;
    private final double sigma;

    public GaussianBumpGround(double baseY, double centerX, double amplitude, double sigma) {
        this.baseY = baseY;
        this.centerX = centerX;
        this.amplitude = amplitude;
        this.sigma = sigma;
    }

    @Override
    public double height(double x) {
        double dx = x - centerX;
        return baseY + amplitude * Math.exp(-(dx * dx) / (2.0 * sigma * sigma));
    }
}
