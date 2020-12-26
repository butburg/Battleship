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

    public boolean[] getHurt() {
        return hurt;
    }

    boolean[] hurt;// = {true,true};


    public ShipImpl(Shipmodel model) {
        this.model = model;
        this.size = model.returnSize();
        position = new Coordinate[size];
        hurt = new boolean[size];
    }

    @Override
    public boolean hit(int field) throws ShipException {
        if (field >= size || field < 0) throw new ShipException(ExceptionMsg.sh_shipFieldInvalid);
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
        if (!foundCoordinate) throw new ShipException(ExceptionMsg.sh_shipUnawareOfCoordinate);
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
    public void locate(int field, int located_x, int located_y) throws ShipException {
        if (field >= size || field < 0) throw new ShipException(ExceptionMsg.sh_shipFieldInvalid);
        Coordinate xy = new Coordinate(located_x, located_y);
        if (anchor == null) anchor = xy;
        else validate(field, located_x, located_y);
        position[field] = xy;
    }

    private void validate(int field, int located_x, int located_y) throws ShipException {
        // if the anchor is already set in ship, the field shouldn't be zero, because this would be the anchor
        if (field < 1)
            throw new ShipException(ExceptionMsg.sh_wrongLocate);

        // the field should not be already set
        if (position[field] != null)
            throw new ShipException(ExceptionMsg.sh_wrongLocate);

        // the location should be in range of the size relative to the anchor
        if ((located_x - anchor.x) >= size || (located_y - anchor.y) >= size)
            throw new ShipException(ExceptionMsg.sh_wrongLocate);

        // the location x and y should be larger than the anchor, when the other location is the same as the anchor is(as it should)
        if (located_x <= anchor.x && located_y == anchor.y || located_y <= anchor.y && located_x == anchor.x)
            throw new ShipException(ExceptionMsg.sh_wrongLocate);

        // one Dimension should stay like the anchors one, means x or y is zero to anchor
        if ((located_x - anchor.x) != 0 && (located_y - anchor.y) != 0)
            throw new ShipException(ExceptionMsg.sh_wrongLocate);

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
