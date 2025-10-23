import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class SimpleDrawingApp extends JFrame {
    private Color currentColor = Color.BLACK;
    private int currentStroke = 2;
    private final DrawArea drawArea = new DrawArea();

    public SimpleDrawingApp() {
        setTitle("Simple Drawing App");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel topPanel = new JPanel();

        // Color chooser
        JButton colorButton = new JButton("Choose Color");
        colorButton.addActionListener(e -> {
            Color selectedColor = JColorChooser.showDialog(null, "Choose a color", currentColor);
            if (selectedColor != null) {
                currentColor = selectedColor;
                drawArea.setColor(currentColor);
            }
        });

        // Stroke size selector
        JLabel strokeLabel = new JLabel("Stroke:");
        String[] strokeOptions = {"1", "2", "4", "6", "8", "10"};
        JComboBox<String> strokeCombo = new JComboBox<>(strokeOptions);
        strokeCombo.setSelectedItem("2");
        strokeCombo.addActionListener(e -> {
            try {
                currentStroke = Integer.parseInt((String) strokeCombo.getSelectedItem());
                drawArea.setStroke(currentStroke);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid stroke size.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        topPanel.add(colorButton);
        topPanel.add(strokeLabel);
        topPanel.add(strokeCombo);

        add(topPanel, BorderLayout.NORTH);
        add(drawArea, BorderLayout.CENTER);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new SimpleDrawingApp().setVisible(true));
    }

    // Custom panel for drawing
    static class DrawArea extends JPanel {
        private final ArrayList<Line> lines = new ArrayList<>();
        private Point startPoint = null;
        private Color color = Color.BLACK;
        private int stroke = 2;

        public DrawArea() {
            setBackground(Color.WHITE);
            MouseAdapter mouseAdapter = new MouseAdapter() {
                public void mousePressed(MouseEvent e) {
                    startPoint = e.getPoint();
                }

                public void mouseDragged(MouseEvent e) {
                    Point endPoint = e.getPoint();
                    if (startPoint != null) {
                        lines.add(new Line(startPoint, endPoint, color, stroke));
                        startPoint = endPoint;
                        repaint();
                    }
                }

                public void mouseReleased(MouseEvent e) {
                    startPoint = null;
                }
            };
            addMouseListener(mouseAdapter);
            addMouseMotionListener(mouseAdapter);
        }

        public void setColor(Color c) {
            this.color = c;
        }

        public void setStroke(int s) {
            this.stroke = s;
        }

        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            for (Line line : lines) {
                g2.setColor(line.color);
                g2.setStroke(new BasicStroke(line.stroke));
                g2.drawLine(line.start.x, line.start.y, line.end.x, line.end.y);
            }
        }

        static class Line {
            Point start, end;
            Color color;
            int stroke;

            Line(Point s, Point e, Color c, int stroke) {
                this.start = s;
                this.end = e;
                this.color = c;
                this.stroke = stroke;
            }
        }
    }
}