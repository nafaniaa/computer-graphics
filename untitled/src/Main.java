import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Main extends JFrame {
    private DrawPanel drawingArea = new DrawPanel();
    private JTextField scaleField;

    public Main() {
        setTitle("Raster Algorithms");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 800);
        setLocationRelativeTo(null);

        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new BorderLayout(10, 10));

        // Drop-down list for algorithm selection
        JPanel algorithmPanel = new JPanel();
        algorithmPanel.setLayout(new BoxLayout(algorithmPanel, BoxLayout.Y_AXIS));
        algorithmPanel.setBorder(BorderFactory.createTitledBorder("Algorithm"));

        JComboBox<String> algorithmComboBox = new JComboBox<>();
        algorithmComboBox.addItem("Step-by-step");
        algorithmComboBox.addItem("DDA");
        algorithmComboBox.addItem("Bresenham (Line)");
        algorithmComboBox.addItem("Bresenham (Circle)");

        algorithmPanel.add(algorithmComboBox);

        // Input panel for coordinates
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(2, 6, 10, 10));

        JTextField x1Field = createFixedSizeTextField("0");
        JTextField y1Field = createFixedSizeTextField("0");
        JTextField x2Field = createFixedSizeTextField("0");
        JTextField y2Field = createFixedSizeTextField("0");
        JTextField radiusField = createFixedSizeTextField("0");

        JLabel x1Label = createFixedSizeLabel("X1:");
        JLabel y1Label = createFixedSizeLabel("Y1:");
        JLabel x2Label = createFixedSizeLabel("X2:");
        JLabel y2Label = createFixedSizeLabel("Y2:");
        JLabel radiusLabel = createFixedSizeLabel("Radius:");

        JLabel scaleLabel = new JLabel("Scale:");
        scaleField = createFixedSizeTextField("15");

        inputPanel.add(x1Label);
        inputPanel.add(x1Field);
        inputPanel.add(y1Label);
        inputPanel.add(y1Field);
        inputPanel.add(x2Label);
        inputPanel.add(x2Field);
        inputPanel.add(y2Label);
        inputPanel.add(y2Field);
        inputPanel.add(radiusLabel);
        inputPanel.add(radiusField);
        inputPanel.add(scaleLabel);
        inputPanel.add(scaleField);

        JButton drawButton = new JButton("Draw");
        JButton applyScaleButton = new JButton("Apply Scale");

        algorithmComboBox.addActionListener(e -> {
            boolean isCircle = algorithmComboBox.getSelectedItem().equals("Bresenham (Circle)");

            radiusField.setVisible(isCircle);
            radiusLabel.setVisible(isCircle);
            x2Field.setVisible(!isCircle);
            y2Field.setVisible(!isCircle);
            x2Label.setVisible(!isCircle);
            y2Label.setVisible(!isCircle);

            inputPanel.revalidate();
            inputPanel.repaint();
        });

        controlPanel.add(algorithmPanel, BorderLayout.NORTH);
        controlPanel.add(inputPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.add(drawButton);
        buttonPanel.add(applyScaleButton);
        controlPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(controlPanel, BorderLayout.WEST);
        add(drawingArea, BorderLayout.CENTER);

        drawButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int x1 = parseInput(x1Field.getText());
                int y1 = parseInput(y1Field.getText());
                int x2 = parseInput(x2Field.getText());
                int y2 = parseInput(y2Field.getText());
                int radius = parseInput(radiusField.getText());

                int scale = parseScaleInput(scaleField.getText());
                String selectedAlgorithm = (String) algorithmComboBox.getSelectedItem();

                drawingArea.draw(selectedAlgorithm, x1, y1, x2, y2, radius, scale);
            }
        });

        applyScaleButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int scale = parseScaleInput(scaleField.getText());
                drawingArea.setScale(scale);
            }
        });
    }

    private int parseInput(String text) {
        if (text.isEmpty()) {
            return 0;
        }
        try {
            return Integer.parseInt(text);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    private int parseScaleInput(String text) {
        if (text.isEmpty()) {
            return 15;
        }
        try {
            int scale = Integer.parseInt(text);
            if (scale < 1) {
                return 15;
            }
            return scale;
        } catch (NumberFormatException e) {
            return 15;
        }
    }

    private JTextField createFixedSizeTextField(String initialValue) {
        JTextField textField = new JTextField(initialValue, 10);
        textField.setPreferredSize(new Dimension(50, 15));
        textField.setMinimumSize(new Dimension(50, 15));
        textField.setMaximumSize(new Dimension(50, 15));
        return textField;
    }

    private JLabel createFixedSizeLabel(String text) {
        JLabel label = new JLabel(text);
        label.setPreferredSize(new Dimension(50, 30));
        return label;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Main().setVisible(true);
            }
        });
    }
}