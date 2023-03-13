package dev.pascan.robotsim;

import dev.pascan.Entity;
import dev.pascan.Mouse;

import java.awt.*;
import java.awt.geom.GeneralPath;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;

public class Graph extends Entity {
    Point TLPos;
    double setpoint, yMIN, yMAX, xSize, xWidth, value;
    int windowWidth, windowHeight;
    List<Double> values = new ArrayList<>();
    double xOffset = 0.0;
    double speed = 1;

    public Graph(Point TLPos, double setpoint, double yMIN, double yMAX, double xSize, double xWidth, int windowWidth, int windowHeight) {
        this.TLPos = TLPos;
        this.setpoint = setpoint;
        this.yMIN = yMIN;
        this.yMAX = yMAX;
        this.xSize = xSize;
        this.xWidth = xWidth;
        this.windowWidth = windowWidth;
        this.windowHeight = windowHeight;
    }

    public Point screenPos(Point p) {
        return new Point(p.x + TLPos.x, p.y + TLPos.y);
    }

    @Override
    public void update() {
        // add a new value to the list
        value = setpoint + (Math.random() - 0.5) * 10;
        values.add(value);
        // if there are more than xWidth values, remove the oldest one
        if (values.size() > xWidth) {
            values.remove(0);
        }
        // update the x offset based on the speed
        xOffset += speed * xSize;
    }

    @Override
    public void draw(Graphics2D g) {
        // set up the drawing parameters
        g.setColor(Color.BLACK);
        g.setStroke(new BasicStroke(2));
        Font font = new Font("Arial", Font.PLAIN, 12);
        g.setFont(font);

        // calculate the y scale factor based on the y range and window size
        double yScale = windowHeight / (yMAX - yMIN);

        // calculate the x position of the left edge of the graph based on the current time and the x size and width
        double leftX = TLPos.x + xOffset - (xSize * xWidth);

        // draw the y axis
        g.drawLine(TLPos.x, TLPos.y, TLPos.x, TLPos.y + windowHeight);

        // draw the x axis
        g.drawLine((int) leftX, TLPos.y + windowHeight, (int) (leftX + (xSize * xWidth)), TLPos.y + windowHeight);

        // draw the y axis labels
        for (double y = yMIN; y <= yMAX; y += (yMAX - yMIN) / 10) {
            double yScreenPos = TLPos.y + windowHeight - ((y - yMIN) * yScale);
            g.drawLine(TLPos.x - 5, (int) yScreenPos, TLPos.x, (int) yScreenPos);
            g.drawString(String.format("%.1f", y), TLPos.x - 40, (int) yScreenPos + 4);
        }

        // iterate over the values and draw the line graph
        double lastX = Double.NaN;
        double lastY = Double.NaN;
        for (int i = 0; i < values.size(); i++) {
            // calculate the x position of the point based on the index and x size
            double x = leftX + (i * xSize);
            // calculate the y position of the point based on the value and y scale factor
            double y = TLPos.y + windowHeight - ((values.get(i) - yMIN) * yScale);
            // if this isn't the first point, draw a line from the last point to this point
            if (!Double.isNaN(lastX)) {
                g.drawLine((int) lastX, (int) lastY, (int) x, (int) y);
            }
            lastX = x;
            lastY = y;
        }
    }

}
