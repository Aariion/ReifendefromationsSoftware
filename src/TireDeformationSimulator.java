public class TireDeformationSimulator {
    private final int n;
    private final double radius0;
    private final double nodeMass;
    private final double kTangential;
    private final double kRadial;
    private final double cNode;
    private final double kGround;
    private final double cGround;
    private final double muDamping;
    private final double gravity;

    private final Vec2[] pos;
    private final Vec2[] vel;

    private Vec2 center;
    private Vec2 centerVel;

    private final double tangentialRestLength;

    public TireDeformationSimulator(int n,
                                    double radius0,
                                    double nodeMass,
                                    double kTangential,
                                    double kRadial,
                                    double cNode,
                                    double kGround,
                                    double cGround,
                                    double muDamping,
                                    double gravity,
                                    Vec2 centerInit) {
        this.n = n;
        this.radius0 = radius0;
        this.nodeMass = nodeMass;
        this.kTangential = kTangential;
        this.kRadial = kRadial;
        this.cNode = cNode;
        this.kGround = kGround;
        this.cGround = cGround;
        this.muDamping = muDamping;
        this.gravity = gravity;
        this.center = centerInit;
        this.centerVel = new Vec2(0.0, 0.0);

        this.pos = new Vec2[n];
        this.vel = new Vec2[n];

        for (int i = 0; i < n; i++) {
            double theta = 2.0 * Math.PI * i / n;
            double x = centerInit.x + radius0 * Math.cos(theta);
            double y = centerInit.y + radius0 * Math.sin(theta);
            pos[i] = new Vec2(x, y);
            vel[i] = new Vec2(0.0, 0.0);
        }

        this.tangentialRestLength = 2.0 * radius0 * Math.sin(Math.PI / n);
    }

    public void setCenterVelocity(double vx, double vy) {
        this.centerVel = new Vec2(vx, vy);
    }

    public SimulationResult run(GroundProfile ground, double dt, int steps, double verticalLoad) {
        double maxDeflection = 0.0;

        for (int s = 0; s < steps; s++) {
            center = center.add(centerVel.scale(dt));

            Vec2[] forces = new Vec2[n];
            for (int i = 0; i < n; i++) {
                forces[i] = new Vec2(0.0, -nodeMass * gravity);
            }

            // Uniform share of vertical external load (downward).
            double perNodeLoad = verticalLoad / n;
            for (int i = 0; i < n; i++) {
                forces[i] = forces[i].add(new Vec2(0.0, -perNodeLoad));
            }

            // Tangential springs to neighboring nodes.
            for (int i = 0; i < n; i++) {
                int j = (i + 1) % n;
                Vec2 d = pos[j].sub(pos[i]);
                double len = d.norm();
                if (len > 1e-12) {
                    Vec2 dir = d.scale(1.0 / len);
                    double ext = len - tangentialRestLength;
                    Vec2 f = dir.scale(kTangential * ext);
                    forces[i] = forces[i].add(f);
                    forces[j] = forces[j].sub(f);
                }
            }

            // Radial springs and damping, plus contact.
            for (int i = 0; i < n; i++) {
                Vec2 rc = pos[i].sub(center);
                double r = rc.norm();
                Vec2 er = (r > 1e-12) ? rc.scale(1.0 / r) : new Vec2(0.0, 1.0);

                double dr = r - radius0;
                Vec2 fRad = er.scale(-kRadial * dr);
                forces[i] = forces[i].add(fRad);

                // Node damping
                forces[i] = forces[i].add(vel[i].scale(-cNode));

                // Ground contact (penalty)
                double groundY = ground.height(pos[i].x);
                double penetration = groundY - pos[i].y;
                if (penetration > 0.0) {
                    double vn = vel[i].y;
                    double fn = kGround * penetration - cGround * Math.min(vn, 0.0);
                    if (fn < 0.0) fn = 0.0;
                    Vec2 fContact = new Vec2(-muDamping * vel[i].x, fn);
                    forces[i] = forces[i].add(fContact);
                }
            }

            // Semi-implicit Euler integration.
            for (int i = 0; i < n; i++) {
                Vec2 acc = forces[i].scale(1.0 / nodeMass);
                vel[i] = vel[i].add(acc.scale(dt));
                pos[i] = pos[i].add(vel[i].scale(dt));

                // Track radial deflection from nominal radius.
                double ri = pos[i].sub(center).norm();
                double defl = Math.max(0.0, radius0 - ri);
                if (defl > maxDeflection) maxDeflection = defl;
            }
        }

        double contactPatch = estimateContactPatchLength(ground);
        return new SimulationResult(maxDeflection, contactPatch, center.y);
    }

    private double estimateContactPatchLength(GroundProfile ground) {
        double minX = Double.POSITIVE_INFINITY;
        double maxX = Double.NEGATIVE_INFINITY;
        int count = 0;

        for (int i = 0; i < n; i++) {
            double gap = pos[i].y - ground.height(pos[i].x);
            if (gap <= 1e-3) {
                count++;
                if (pos[i].x < minX) minX = pos[i].x;
                if (pos[i].x > maxX) maxX = pos[i].x;
            }
        }

        if (count < 2) return 0.0;
        return Math.max(0.0, maxX - minX);
    }
}
