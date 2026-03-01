public class Main {
    public static void main(String[] args) {
        int n = 80;
        double radius0 = 0.32;
        double nodeMass = 0.35;
        double kTangential = 1.8e5;
        double kRadial = 2.4e5;
        double cNode = 35.0;
        double kGround = 1.5e6;
        double cGround = 2200.0;
        double muDamping = 140.0;
        double gravity = 9.81;

        double dt = 1e-4;

        // 1) Stationary on flat road
        TireDeformationSimulator stationary = new TireDeformationSimulator(
                n, radius0, nodeMass, kTangential, kRadial, cNode,
                kGround, cGround, muDamping, gravity, new Vec2(0.0, 0.30)
        );
        stationary.setCenterVelocity(0.0, 0.0);
        SimulationResult r1 = stationary.run(new FlatGround(0.0), dt, 45000, 5500.0);

        // 2) Rolling on flat road
        TireDeformationSimulator rollingFlat = new TireDeformationSimulator(
                n, radius0, nodeMass, kTangential, kRadial, cNode,
                kGround, cGround, muDamping, gravity, new Vec2(-1.0, 0.30)
        );
        rollingFlat.setCenterVelocity(8.0, 0.0);
        SimulationResult r2 = rollingFlat.run(new FlatGround(0.0), dt, 35000, 5500.0);

        // 3) Rolling over bump
        TireDeformationSimulator rollingBump = new TireDeformationSimulator(
                n, radius0, nodeMass, kTangential, kRadial, cNode,
                kGround, cGround, muDamping, gravity, new Vec2(-1.0, 0.30)
        );
        rollingBump.setCenterVelocity(8.0, 0.0);
        GroundProfile bump = new GaussianBumpGround(0.0, 0.75, 0.04, 0.10);
        SimulationResult r3 = rollingBump.run(bump, dt, 45000, 5500.0);

        System.out.println("=== Tire deformation demo (2D mass-spring) ===");
        printScenario("Stationary / flat", r1);
        printScenario("Rolling / flat", r2);
        printScenario("Rolling / bump", r3);
    }

    private static void printScenario(String name, SimulationResult r) {
        System.out.printf("%s -> max deflection: %.4f m, contact patch: %.4f m, center y: %.4f m%n",
                name, r.maxDeflection, r.contactPatchLength, r.centerY);
    }
}
