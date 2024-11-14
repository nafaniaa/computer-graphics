import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Main {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Graphics Algorithms");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);

        DrawPanel drawPanel = new DrawPanel();
        frame.add(drawPanel, BorderLayout.CENTER);

        JPanel controlPanel = new JPanel();
        JComboBox<String> algorithmComboBox = new JComboBox<>(new String[]{
                "Step-by-step",
                "DDA",
                "Bresenham (Line)",
                "Bresenham (Circle)",
                "Wu (Anti-Aliased Line)"
        });
        controlPanel.add(algorithmComboBox);

        JTextField x1Field = new JTextField("0", 5);
        JTextField y1Field = new JTextField("0", 5);
        JTextField x2Field = new JTextField("5", 5);
        JTextField y2Field = new JTextField("5", 5);
        JTextField radiusField = new JTextField("10", 5);
        JTextField scaleField = new JTextField("15", 5);

        controlPanel.add(new JLabel("X1:"));
        controlPanel.add(x1Field);
        controlPanel.add(new JLabel("Y1:"));
        controlPanel.add(y1Field);
        controlPanel.add(new JLabel("X2:"));
        controlPanel.add(x2Field);
        controlPanel.add(new JLabel("Y2:"));
        controlPanel.add(y2Field);
        controlPanel.add(new JLabel("Radius:"));
        controlPanel.add(radiusField);
        controlPanel.add(new JLabel("Scale:"));
        controlPanel.add(scaleField);

        JButton drawButton = new JButton("Draw");
        controlPanel.add(drawButton);

        drawButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String algorithm = (String) algorithmComboBox.getSelectedItem();
                int x1 = Integer.parseInt(x1Field.getText());
                int y1 = Integer.parseInt(y1Field.getText());
                int x2 = Integer.parseInt(x2Field.getText());
                int y2 = Integer.parseInt(y2Field.getText());
                int radius = Integer.parseInt(radiusField.getText());
                int scale = Integer.parseInt(scaleField.getText());

                drawPanel.draw(algorithm, x1, y1, x2, y2, radius, scale);
            }
        });

        frame.add(controlPanel, BorderLayout.SOUTH);
        frame.setVisible(true);
    }
}
