package ship;

import exceptions.ExceptionMessages;
import exceptions.ShipException;

import java.awt.*;

/**
 * @author Edwin W (570900) on Nov 2020
 * This is an implementation for the ship.
 */
public class ShipImpl implements Ship {

    final Shipmodel model;
    private final int size;
    Point anchor;// = {{2,3},{2,4}};
    Point[] position;// = {{2,3},{2,4}};
    boolean[] hurt;// = {true,true};

    /**
     * Constructor for the tests.
     * @param model
     * @param x
     * @param y
     * @param vertical
     * @throws ShipException
     */
    public ShipImpl(Shipmodel model, int x, int y, boolean vertical) throws ShipException {
        this.model = model;
        this.size = model.returnSize();
        hurt = new boolean[size];
        this.anchor = new Point(x, y);
        createPosition(x, y, vertical);
    }

    public ShipImpl(Shipmodel model, int x, int y) throws ShipException {
        this(model, x, y, false);
    }

    public ShipImpl(Shipmodel model) throws ShipException {
        this.model = model;
        this.size = model.returnSize();
        hurt = new boolean[size];
    }

    @Override
    public boolean hit(int field) {
        hurt[field] = true;
        return sunk();
    }

    @Override
    public boolean sunk() {
        for (boolean part : hurt) {
            if (!part) return false;
        }
        return true;
    }

    @Override
    public Point[] createPosition(int x, int y, boolean vertical) throws ShipException {
        if (x < 0 || y < 0) throw new ShipException(ExceptionMessages.shipInvalidPosition);
        this.anchor = new Point(x, y);
        int inc_x = 0;
        int inc_y = 0;

        if (vertical) inc_y = 1;
        else inc_x = 1;

        Point[] position = new Point[model.returnSize()];

        for (int i = 0; i < size; i++) {
            position[i] = new Point(x, y);
            x += inc_x;
            y += inc_y;
        }
        this.position = position;
        return position;
    }

    @Override
    public Point getAnchor() {
        return new Point(anchor);
    }

    @Override
    public int getSize() {
        return size;
    }

    @Override
    public Point[] getPosition() {
        return position;
    }

    @Override
    public Shipmodel getModel() {
        return model;
    }
}
