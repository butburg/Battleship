package ship;

import exceptions.ExceptionMsg;
import exceptions.ShipException;
import field.Coordinate;

/**
 * @author Edwin W (HTW) on Nov 2020
 * This is an implementation for the ship.
 */
public class ShipImpl implements Ship {

    final Shipmodel model;
    private final int size;
    Coordinate anchor;// = {{2,3},{2,4}};
    Coordinate[] position;// = {{2,3},{2,4}};
    boolean[] hurt;// = {true,true};


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
            if (shipFieldPosition.equals(hitHere)) {
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
    public void locate(int field, int located_x, int located_y) {
        position[field] = new Coordinate(located_x, located_y);
    }

    @Override
    public Coordinate getAnchor() {
        return new Coordinate(anchor);
    }

    @Override
    public int getSize() {
        return size;
    }

    @Override
    public Coordinate[] getPosition() {
        return position;
    }

    @Override
    public Shipmodel getModel() {
        return model;
    }
}
