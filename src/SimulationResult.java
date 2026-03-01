public class SimulationResult {
    public final double maxDeflection;
    public final double contactPatchLength;
    public final double centerY;

    public SimulationResult(double maxDeflection, double contactPatchLength, double centerY) {
        this.maxDeflection = maxDeflection;
        this.contactPatchLength = contactPatchLength;
        this.centerY = centerY;
    }
}
