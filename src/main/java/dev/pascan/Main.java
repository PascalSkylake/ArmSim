package dev.pascan;

import dev.pascan.robotsim.Field;
import dev.pascan.robotsim.Graph;
import dev.pascan.robotsim.Robot;

import dev.pascan.robotsim.Arm;

import java.awt.*;

public class Main {
    public static Simulator sim;
    public static Robot robot;
    public static Arm arm;
    public static Field field;
    public static Graph graph1;

    public static void main(String[] arsg) {
        sim = new Simulator("Arm Simulator", false);
        robot = new Robot(100, 100);
        arm = new Arm();
        field = new Field();
        graph1 = new Graph(new Point(500, 500), Mouse.x, 0, 1920, 1, 10, 900, 500);

        sim.createMouseListener();
        sim.createKeyboardListener();


        EntityHandler.addEntity(arm);
        EntityHandler.addEntity(graph1);
    }
}
