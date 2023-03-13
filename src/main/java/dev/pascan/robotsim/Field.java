package dev.pascan.robotsim;

import dev.pascan.*;
import dev.pascan.Window;

import java.awt.*;
import java.awt.event.KeyEvent;

public class Field extends Entity {
    public static Point windowTL = new Point(20, 50);
    public static Point windowBR = new Point(Window.WIDTH - 310, Window.HEIGHT - 20);

    public static int zoomFactor = 1;
    public static MouseState previous;
    public static Point mousePos;
    public static Point mouseStart;
    public static Point fieldCenter;
    public static Point fieldCenterStart;

    public Field() {
        fieldCenter = new Point((windowBR.x / 2) + windowTL.x, (windowBR.y / 2) + windowTL.y);
        previous = MouseState.RELEASED;
        x = windowTL.x;
        y = windowTL.y;
        width = windowBR.x - windowTL.x;
        height = windowBR.y - windowTL.y;
    }

    public Point fieldCenterPosToScreenPos(Point p) {
        return new Point((p.x + fieldCenter.x), (p.y + fieldCenter.y));
    }



    @Override
    public void update() {
        if (Mouse.x >= windowTL.x && Mouse.y >=  windowTL.y && Mouse.x <= windowBR.x && Mouse.y <= windowBR.y) {
            int xdiff, ydiff;
            if (Mouse.amountScrolled != zoomFactor) {
                zoomFactor = Mouse.amountScrolled;
            }
            if (((Mouse.state.equals(MouseState.PRESSED) || Mouse.state.equals(MouseState.DRAGGED)) && (previous.equals(MouseState.MOVED) || previous.equals(MouseState.RELEASED)))) {
                mouseStart = new Point(Mouse.x, Mouse.y);
                mousePos = new Point(Mouse.x, Mouse.y);
                fieldCenterStart = new Point(fieldCenter);
            } else if (Mouse.state.equals(MouseState.DRAGGED)) {
                mousePos = new Point(Mouse.x, Mouse.y);
                xdiff = mouseStart.x - mousePos.x;
                ydiff = mouseStart.y - mousePos.y;
                fieldCenter = new Point(fieldCenterStart.x - xdiff, fieldCenterStart.y - ydiff);
            }
            xdiff = 0;
            ydiff = 0;
            previous = Mouse.state;


            if (Key.isKeyPressed(KeyEvent.VK_PLUS)) {
                zoomFactor++;
            }
            if (Key.isKeyPressed(KeyEvent.VK_MINUS)) {
                if (zoomFactor > 1) {
                    zoomFactor--;
                }
            }
        }
    }

    @Override
    public void draw(Graphics2D g) {
        g.setColor(Color.WHITE);
        g.drawRect((int) windowTL.x, (int) windowTL.y, (int) (windowBR.x - windowTL.x), (int) (windowBR.y - windowTL.y));
        g.drawRect(fieldCenter.x - 10, fieldCenter.y - 10, 20, 20);
        //g.fillRect(fieldPosToScreenPos(new Point(100, 100)).x, fieldPosToScreenPos(new Point(100, 100)).y, 50, 50);
    }
}
