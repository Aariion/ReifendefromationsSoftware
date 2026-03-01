import javax.swing.*;
import java.awt.BorderLayout;

public class Simulation {

    public static void main(String[] args) {

        Tire tire = new Tire();
        Road road = new Road();

        tire.setLoad(3500f);        // normal load
        tire.setRotationSpeed(10f); // initial speed

        TirePanel panel = new TirePanel(tire, road);

        JFrame frame = new JFrame("Tire Simulation");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.add(panel, BorderLayout.CENTER);

        JPanel controlPanel = new JPanel();
        JLabel speedLabel = new JLabel("Tire Speed (rad/s): ");
        JTextField speedField = new JTextField("10", 6);
        controlPanel.add(speedLabel);
        controlPanel.add(speedField);
        frame.add(controlPanel, BorderLayout.SOUTH);
        frame.pack();
        frame.setVisible(true);

        speedField.addActionListener(e -> {
            try {
                float speed = Float.parseFloat(speedField.getText());
                tire.setRotationSpeed(speed);
            } catch (NumberFormatException ex) {
                // ignore invalid input
            }
        });

        new Timer(16, e -> {
            tire.update(0.016f, road);
            panel.repaint();
        }).start();
    }
}