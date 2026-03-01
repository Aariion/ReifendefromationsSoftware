import java.util.Random;

public class TestRoad {
    public static void main(String[] args) {
        float[] heights = new float[20000];
        Random rnd = new Random(12345);
        heights[0] = 0f;
        for (int i = 1; i < heights.length; i++) {
            heights[i] = heights[i - 1] + (rnd.nextFloat() * 2 - 1) * 0.1f;
        }
        float min=Float.MAX_VALUE, max=-Float.MAX_VALUE;
        for (float h : heights) { min=Math.min(min,h); max=Math.max(max,h);}        
        System.out.println("min="+min+" max="+max);
    }
}