public class Tire {

    // --- Physical properties ---
    private final float restRadius = 0.35f;         // meters, undeformed
    private float centerY;       // initial height
    private float load = 0f;                       // load on tire

    private final int segments = 72;
    private final float[] segVel;
    private final float[] segRadius;

    private float deformation = 0f;           // max compression from restRadius

    private float rotationSpeed = 0f;         // rad/s around center
    private float rotationAngle = 0f;         // current orientation
    private float positionX = 0f;             // road position

    public Tire() {
        centerY = restRadius;
        segRadius = new float[segments];
        segVel = new float[segments];
        for (int i = 0; i < segments; i++) {
            segRadius[i] = restRadius;
            segVel[i] = 0f;
        }
    }

    public void update(float deltaTime, Road road) {
        // make sure the wheel center doesn't sink below the undeformed radius
        if (centerY < restRadius) {
            centerY = restRadius;
        }

        // reset deformation each frame
        deformation = 0f;

        // ...existing code...

        // advance rotation
        rotationAngle += rotationSpeed * deltaTime;

        // process each segment
        float minR = restRadius * 0.9f;            // don't squish more than 10%
        deformation = 0f;
        for (int i = 0; i < segments; i++) {
            float angle = rotationAngle + (float) i / segments * (float) (2 * Math.PI);
            float r = segRadius[i];
            float dx = r * (float) Math.cos(angle);
            float dy = r * (float) Math.sin(angle);
            float worldX = positionX + dx;
            float worldY = centerY + dy;
            float roadH = road.getHeight(worldX);
            float penetration = roadH - worldY;
            if (penetration > 0.001f && dy < 0) {
                if (penetration > restRadius * 0.1f) penetration = restRadius * 0.1f;
                // Deformation increases with speed
                float speedFactor = 1.0f + Math.abs(rotationSpeed) * 0.05f;
                float targetR = restRadius - penetration * speedFactor;
                if (targetR < minR) targetR = minR;
                segRadius[i] = targetR;
                deformation = Math.max(deformation, restRadius - segRadius[i]);
            } else {
                segRadius[i] = restRadius;
            }
        }

        // Always keep tire on the ground
        float roadH = road.getHeight(positionX);
        centerY = roadH + restRadius;

        // Physical constraint: reset any non-physical segment radius
        for (int i = 0; i < segments; i++) {
            if (segRadius[i] <= 0 || segRadius[i] > restRadius * 2f || Float.isNaN(segRadius[i]) || Float.isInfinite(segRadius[i])) {
                segRadius[i] = restRadius;
            }
        }

        // update horizontal travel using restRadius
        float effectiveRadius = restRadius;

        // update horizontal travel using effective radius
        positionX += rotationSpeed * effectiveRadius * deltaTime;
    }

    // --- getters for drawing & inspection ---
    public float getCenterY() { return centerY; }

    public float getRestRadius() { return restRadius; }

    public int getSegmentCount() { return segments; }

    public float getSegmentX(int i) {
        float angle = rotationAngle + (float) i / segments * (float) (2 * Math.PI);
        return segRadius[i] * (float) Math.cos(angle);
    }

    public float getSegmentY(int i) {
        float angle = rotationAngle + (float) i / segments * (float) (2 * Math.PI);
        return segRadius[i] * (float) Math.sin(angle);
    }

    public float getPositionX() { return positionX; }

    public float getDeformation() { return deformation; }

    public void setLoad(float load) {
        this.load = load;
    }

    public float getLoad() {
        return load;
    }

    // --- controls ---
    public void setRotationSpeed(float speed) { this.rotationSpeed = speed; }
}
