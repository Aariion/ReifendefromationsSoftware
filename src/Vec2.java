public class Vec2 {
    public double x;
    public double y;

    public Vec2(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Vec2 add(Vec2 o) { return new Vec2(this.x + o.x, this.y + o.y); }
    public Vec2 sub(Vec2 o) { return new Vec2(this.x - o.x, this.y - o.y); }
    public Vec2 scale(double s) { return new Vec2(this.x * s, this.y * s); }

    public double dot(Vec2 o) { return this.x * o.x + this.y * o.y; }
    public double norm() { return Math.sqrt(x * x + y * y); }

    public Vec2 normalized() {
        double n = norm();
        if (n < 1e-12) return new Vec2(0.0, 0.0);
        return scale(1.0 / n);
    }
}
