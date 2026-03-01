import java.util.Random;

public class Road {

    // random height samples, linear interp between them
    private static final float SPACING = 0.1f; // meters between samples (more bumps)
    private static final int POINTS = 20000;
    private float[] heights;

    public Road() {
        heights = new float[POINTS];
        Random rnd = new Random(12345);
        heights[0] = 0f;
        for (int i = 1; i < POINTS; i++) {
            // step up/down up to 10cm per sample
            float next = heights[i - 1] + (rnd.nextFloat() * 2 - 1) * 0.1f;
            // clamp total to +/- 20cm
            if (next > 0.2f) next = 0.2f;
            if (next < -0.2f) next = -0.2f;
            heights[i] = next;
        }
        // smooth with simple moving average
        for (int i = 2; i < POINTS - 2; i++) {
            heights[i] = (heights[i - 2] + heights[i - 1] + heights[i] + heights[i + 1] + heights[i + 2]) / 5f;
        }
    }

    public float getHeight(float x) {
        if (x < 0) x = 0;
        float idxf = x / SPACING;
        int idx = (int) Math.floor(idxf);
        if (idx >= POINTS - 1) {
            return heights[POINTS - 1];
        }
        float frac = idxf - idx;
        return heights[idx] * (1 - frac) + heights[idx + 1] * frac;
    }
}
