package ship;

import exceptions.ShipException;
import game.Shipmodel;

import java.awt.*;

/**
 * @author Edwin W (570900) on Nov 2020
 */
public class Ship {

    final Shipmodel model;
    private final int size;
    Point ancher;// = {{2,3},{2,4}};
    Point[] position;// = {{2,3},{2,4}};

    public Ship(Shipmodel model, int x, int y) throws ShipException {
        this.model = model;
        this.size = model.returnSize();
        this.ancher = new Point(x, y);
    }

    public Point[] createPosition(Point anchor, boolean vertical) {
        int inc_x = 0;
        int inc_y = 0;

        if (vertical) inc_x = 1;
        else inc_y = 1;

        Point[] points = new Point[model.returnSize()];

        for (int i = 0; i < size; i++) {
            points[i] = anchor;
            anchor.x = +inc_x;
            anchor.y = +inc_y;
        }
        return points;
    }

    public Point getAncher() {
        return ancher;
    }

    public int getSize() {
        return size;
    }

    public Point[] getPosition() {
        return position;
    }

    public Shipmodel getModel() {
        return model;
    }
}
