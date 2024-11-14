import javax.swing.*;
import java.awt.*;

class DrawPanel extends JPanel {
    private int scale = 15;
    private String currentAlgorithm;
    private int x1, y1, x2, y2, radius;

    public DrawPanel() {
        setBackground(Color.WHITE);
    }

    public void setScale(int scale) {
        this.scale = scale;
        repaint();
    }

    public void draw(String algorithm, int x1, int y1, int x2, int y2, int radius, int scale) {
        this.currentAlgorithm = algorithm;
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
        this.radius = radius;
        this.scale = scale;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawCoordinatePlane(g);

        if (currentAlgorithm == null) {
            return;
        }

        int centerX = getWidth() / 2;
        int centerY = getHeight() / 2;

        switch (currentAlgorithm) {
            case "Step-by-step":
                drawStepByStep(g, x1, y1, x2, y2, centerX, centerY);
                break;
            case "DDA":
                drawDDA(g, x1, y1, x2, y2, centerX, centerY);
                break;
            case "Bresenham (Line)":
                drawBresenhamLine(g, x1, y1, x2, y2, centerX, centerY);
                break;
            case "Bresenham (Circle)":
                drawCircleBresenham(g, x1, y1, radius, centerX, centerY);
                break;
            case "Wu (Anti-Aliased Line)":
                drawWuLine(g, x1, y1, x2, y2, centerX, centerY);
                break;
        }
    }

    private void drawCoordinatePlane(Graphics g) {
        g.setColor(Color.GREEN); // Green axes
        int width = getWidth();
        int height = getHeight();
        int centerX = width / 2;
        int centerY = height / 2;

        for (int x = centerX % scale; x < width; x += scale) {
            g.drawLine(x, 0, x, height);
        }
        for (int y = centerY % scale; y < height; y += scale) {
            g.drawLine(0, y, width, y);
        }

        g.setColor(Color.BLACK);
        g.drawLine(centerX, 0, centerX, height);
        g.drawLine(0, centerY, width, centerY);

        // Add axis labels
        g.drawString("X", width - 10, centerY - 5);
        g.drawString("Y", centerX + 5, 10);

        g.setFont(new Font("Arial", Font.PLAIN, 10));

        for (int x = centerX; x < width; x += scale) {
            g.drawLine(x, centerY - 5, x, centerY + 5);
            if ((x - centerX) / scale != 0) {
                g.drawString(String.valueOf((x - centerX) / scale), x - 5, centerY + 15);
            }
        }
        for (int x = centerX - scale; x > 0; x -= scale) {
            g.drawLine(x, centerY - 5, x, centerY + 5);
            g.drawString(String.valueOf((x - centerX) / scale), x - 10, centerY + 15);
        }

        for (int y = centerY; y < height; y += scale) {
            g.drawLine(centerX - 5, y, centerX + 5, y);
            if ((y - centerY) / scale != 0) {
                g.drawString(String.valueOf((centerY - y) / scale), centerX + 10, y + 5);
            }
        }
        for (int y = centerY - scale; y > 0; y -= scale) {
            g.drawLine(centerX - 5, y, centerX + 5, y);
            g.drawString(String.valueOf((centerY - y) / scale), centerX + 10, y + 5);
        }
    }

    private void drawStepByStep(Graphics g, int x1, int y1, int x2, int y2, int centerX, int centerY) {
        g.setColor(Color.RED);
        int dx = x2 - x1;
        int dy = y2 - y1;
        int steps = Math.max(Math.abs(dx), Math.abs(dy));
        float xInc = (float) dx / steps;
        float yInc = (float) dy / steps;
        float x = x1;
        float y = y1;

        for (int i = 0; i <= steps; i++) {
            int plotX = Math.round(centerX + x * scale);
            int plotY = Math.round(centerY - y * scale);
            g.fillRect(plotX, plotY, 15, 15);
            x += xInc;
            y += yInc;
        }
    }

    private void drawDDA(Graphics g, int x1, int y1, int x2, int y2, int centerX, int centerY) {
        g.setColor(Color.BLUE);
        int dx = x2 - x1;
        int dy = y2 - y1;
        int steps = Math.max(Math.abs(dx), Math.abs(dy));
        float xInc = (float) dx / steps;
        float yInc = (float) dy / steps;
        float x = x1;
        float y = y1;

        for (int i = 0; i <= steps; i++) {
            int plotX = Math.round(centerX + x * scale);
            int plotY = Math.round(centerY - y * scale);
            g.fillRect(plotX, plotY, 15, 15);
            x += xInc;
            y += yInc;
        }
    }

    private void drawBresenhamLine(Graphics g, int x1, int y1, int x2, int y2, int centerX, int centerY) {
        g.setColor(Color.GREEN);
        int dx = x2 - x1;
        int dy = y2 - y1;
        int dx2 = 2 * Math.abs(dx);
        int dy2 = 2 * Math.abs(dy);
        int err = Math.abs(dx) > Math.abs(dy) ? dx2 - Math.abs(dy) : dy2 - Math.abs(dx);
        int e2;

        int x = x1;
        int y = y1;

        int endX = x2;
        int endY = y2;

        if (x1 > x2) {
            x = x2;
            y = y2;
            endX = x1;
            endY = y1;
        }

        while (x <= endX) {
            int plotX = centerX + x * scale;
            int plotY = centerY - y * scale;
            g.fillRect(plotX, plotY, 15, 15);
            e2 = err;
            if (e2 > -dx2) {
                err -= dy2;
                x++;
            }
            if (e2 < dy2) {
                err += dx2;
                y++;
            }
        }
    }

    private void drawCircleBresenham(Graphics g, int x1, int y1, int radius, int centerX, int centerY) {
        g.setColor(Color.MAGENTA);

        int x = radius;
        int y = 0;
        int err = 0;

        while (x >= y) {
            g.fillRect(centerX + x * scale, centerY - y * scale, 15, 15);
            g.fillRect(centerX + y * scale, centerY - x * scale, 15, 15);
            g.fillRect(centerX - y * scale, centerY - x * scale, 15, 15);
            g.fillRect(centerX - x * scale, centerY - y * scale, 15, 15);
            g.fillRect(centerX - x * scale, centerY + y * scale, 15, 15);
            g.fillRect(centerX - y * scale, centerY + x * scale, 15, 15);
            g.fillRect(centerX + y * scale, centerY + x * scale, 15, 15);
            g.fillRect(centerX + x * scale, centerY + y * scale, 15, 15);

            y++;
            err += 2 * y + 1;
            if (err > 0) {
                x--;
                err -= 2 * x + 1;
            }
        }
    }

    private void drawWuLine(Graphics g, int x1, int y1, int x2, int y2, int centerX, int centerY) {
        g.setColor(Color.ORANGE);
        int dx = Math.abs(x2 - x1); //прямая, вдоль которой калдуем
        int dy = Math.abs(y2 - y1);

        boolean steep = dy > dx;

        //Проверка на крутизну линии
        //Больше ли изменение x чем y
        if (steep) {
            int tempX1 = x1;
            x1 = y1;
            y1 = tempX1;

            int tempX2 = x2;
            x2 = y2;
            y2 = tempX2;

            dx = Math.abs(x2 - x1);
            dy = Math.abs(y2 - y1);
        }

        if (x1 > x2) {
            int tempX1 = x1;
            x1 = x2;
            x2 = tempX1;

            int tempY1 = y1;
            y1 = y2;
            y2 = tempY1;
        }

        double gradient = dy / (double) dx; //расчет наклона
        double y = y1;

        for (int x = x1; x <= x2; x++) {
            int plotX = steep ? (int) (centerX + y * scale) : (int) (centerX + x * scale);
            int plotY = steep ? (int) (centerY - x * scale) : (int) (centerY - y * scale);
            g.setColor(new Color(255, 165, 0, (int) (255 * (1 - (y % 1)))));
            g.fillRect(plotX, plotY, 15, 15);

            plotY = steep ? (int) (centerY - x * scale) : (int) (centerY - (y + 1) * scale);
            g.setColor(new Color(255, 165, 0, (int) (255 * (y % 1))));
            g.fillRect(plotX, plotY, 20, 20);

            y += gradient;
        }
    }
}
