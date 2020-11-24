package ship;

import exceptions.ExceptionMsg;
import exceptions.ShipException;
import field.Coordinate;

import java.awt.*;

/**
 * @author Edwin W (570900) on Nov 2020
 * This is an implementation for the ship.
 */
public class ShipImpl implements Ship {

    final Shipmodel model;
    private final int size;
    Point anchor;// = {{2,3},{2,4}};
    Coordinate[] position;// = {{2,3},{2,4}};
    boolean[] hurt;// = {true,true};

    /**
     * Constructor for the tests. Will be removed!
     *
     * @param model    type of the ship
     * @param x        coordinate
     * @param y        coordinate
     * @param vertical true, or false for horizontal
     * @throws ShipException if the ship get a bad position
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

    public ShipImpl(Shipmodel model) {
        this.model = model;
        this.size = model.returnSize();
         position = new Coordinate[size];
        hurt = new boolean[size];
    }

    @Override
    public boolean hit(int field) throws ShipException {
        if (field >= size || field < 0) throw new ShipException(ExceptionMsg.shipFieldInvalid);
        hurt[field] = true;
        return sunk();
    }

    @Override
    public boolean hit(int x, int y) throws ShipException {
        Coordinate hitHere = new Coordinate(x, y);
        int index = 0;
        boolean foundCoordinate = false;
        for (Coordinate shipFieldPosition : position) {
            if (shipFieldPosition == hitHere) {
                hurt[index] = true;
                foundCoordinate = true;
            }
            index++;
        }
        if (!foundCoordinate) throw new ShipException(ExceptionMsg.shipUnawareOfCoordinate);
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
        if (x < 0 || y < 0) throw new ShipException(ExceptionMsg.shipInvalidPosition);
        this.anchor = new Point(x, y);
        int inc_x = 0;
        int inc_y = 0;

        if (vertical) inc_y = 1;
        else inc_x = 1;

        Coordinate[] position = new Coordinate[model.returnSize()];

        for (int i = 0; i < size; i++) {
            position[i] = new Coordinate(x, y);
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

    @Override
    public void locate(int field, int located_x, int located_y) {
        position[field] = new Coordinate(located_x, located_y);
    }
}
