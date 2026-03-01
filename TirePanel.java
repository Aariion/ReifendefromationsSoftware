import java.awt.*;
import java.awt.geom.Path2D;
import javax.swing.*;

public class TirePanel extends JPanel {

    private final Tire tire;
    private final Road road;
    private final int scale = 300; // meters to pixels

    public TirePanel(Tire tire, Road road) {
        this.tire = tire;
        this.road = road;
        setPreferredSize(new Dimension(800, 600));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        int centerX = 400;

        // we will compute vertical offset when drawing the deformable shape

        // Draw road profile based on road height
        Path2D.Float path = new Path2D.Float();
        // start at first pixel
        float worldX = tire.getPositionX() + (0 - centerX) / (float) scale;
        float rh = road.getHeight(worldX);
        float py = 500 - rh * scale;
        path.moveTo(0, py);
        for (int px = 1; px < getWidth(); px++) {
            worldX = tire.getPositionX() + (px - centerX) / (float) scale;
            rh = road.getHeight(worldX);
            py = 500 - rh * scale;
            path.lineTo(px, py);
        }
        // fill below the path
        path.lineTo(getWidth(), getHeight());
        path.lineTo(0, getHeight());
        path.closePath();
        g2.setColor(Color.GRAY);
        g2.fill(path);
        // outline
        g2.setColor(Color.DARK_GRAY);
        g2.draw(path);

        // Get tire parameters
        float centerY = tire.getCenterY();
        float restRadius = tire.getRestRadius();
        int segs = tire.getSegmentCount();

        // Draw deformable tire shape
        g2.setColor(Color.BLACK);
        Path2D.Float tireShape = new Path2D.Float();
        for (int i = 0; i < segs; i++) {
            float sx = tire.getSegmentX(i);
            float sy = tire.getSegmentY(i);
            float px = centerX + sx * scale;
            float py2 = 500 - (centerY + sy) * scale;
            if (i == 0) {
                tireShape.moveTo(px, py2);
            } else {
                tireShape.lineTo(px, py2);
            }
        }
        tireShape.closePath();
        g2.fill(tireShape);

        // Draw tire rim (center hub)
        float rimRadius = restRadius * 0.4f;
        g2.setColor(new Color(200, 200, 200));
        g2.fillOval(centerX - (int)(rimRadius * scale),
                    500 - (int)(centerY * scale) - (int)(rimRadius * scale),
                    (int)(rimRadius * 2 * scale),
                    (int)(rimRadius * 2 * scale));
        g2.setColor(Color.DARK_GRAY);
        g2.drawOval(centerX - (int)(rimRadius * scale),
                    500 - (int)(centerY * scale) - (int)(rimRadius * scale),
                    (int)(rimRadius * 2 * scale),
                    (int)(rimRadius * 2 * scale));

        // Draw tire sidewall (outer circle)
        g2.setColor(new Color(60, 60, 60));
        g2.setStroke(new BasicStroke(4));
        g2.drawOval(centerX - (int)(restRadius * scale),
                    500 - (int)(centerY * scale) - (int)(restRadius * scale),
                    (int)(restRadius * 2 * scale),
                    (int)(restRadius * 2 * scale));
        g2.setStroke(new BasicStroke(1));


        // Draw tire outline
        g2.setColor(Color.BLACK);
        g2.drawOval(centerX - (int)(restRadius * scale),
                    500 - (int)(centerY * scale) - (int)(restRadius * scale),
                    (int)(restRadius * 2 * scale),
                    (int)(restRadius * 2 * scale));
    }
}