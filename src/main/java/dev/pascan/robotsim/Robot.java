package dev.pascan.robotsim;

import dev.pascan.*;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

public class Robot extends Entity {
    public static ArmEntity arm = new ArmEntity();
    private static double maxBaseSide;
    private static Point2D lastPos;
    public static PIDController targetPIDx;
    public static PIDController targetPIDy;
    public static PIDController targetPIDtheta;
    public static ArrayList<Point> trail = new ArrayList<>();

    public Robot(double startX, double startY) {
        super(startX, startY, 32, 28);
        lastPos = new Point2D.Double(startX, startY);
        EntityHandler.addEntity(arm);

        targetPIDx = new PIDController(0.2, 0, 0.0);
        targetPIDy = new PIDController(0.2, 0, 0.0);
        targetPIDtheta = new PIDController(9, 0, 0.0);
        targetPIDtheta.enableContinuousInput(-Math.PI, Math.PI);

        maxBaseSide = Point.distance(x, y, x + width / 2, y + height / 2);
    }

    @Override
    public void update() {



        x += targetPIDx.calculate(x);
        y += targetPIDy.calculate(y);
        angle += targetPIDtheta.calculate(Math.toRadians(angle));


        if (!isTouching(Main.field)) {
            x = lastPos.getX();
            y = lastPos.getY();
        }

        targetPIDx.setSetpoint(Mouse.x);
        targetPIDy.setSetpoint(Mouse.y);
        targetPIDtheta.setSetpoint(Math.atan2(Field.fieldCenter.y - y, Field.fieldCenter.x - x));


        // angle = Math.toDegrees(Math.atan2(Mouse.y - y, Mouse.x - x));
        if (Key.isKeyPressed('A')) {
            x = 50;
            y = 50;
            angle = 0;
        }
        //trail.add(new Point((int) x, (int) y));
        lastPos = new Point2D.Double(this.x, this.y);
    }

    @Override
    public void draw(Graphics2D g) {
        /*
        g.setColor(Color.RED);

        for (Point p : trail) {
            g.fillRect(p.x, p.y, 5, 5);
        }
        */
        g.setColor(Color.WHITE);
        if (targetPIDtheta.atSetpoint() && targetPIDx.atSetpoint() && targetPIDy.atSetpoint()) {
            g.setColor(Color.GREEN);
        }




        // Create a new transform for the group
        AffineTransform transform = new AffineTransform();
        transform.translate(x, y);
        transform.rotate(Math.toRadians(angle));
        transform.translate(-width / 2, -height / 2);

        // Apply the transform to the graphics context
        g.transform(transform);

        // Draw the rectangle and line as part of the group
        Rectangle2D r = new Rectangle2D.Double(0, 0, width, height);
        Line2D line = new Line2D.Double(width / 2, height / 2, width / 2 + 30, height / 2);
        g.draw(r);
        g.draw(line);

        // Reset the transform on the graphics context
        g.setTransform(new AffineTransform());
    }
}
