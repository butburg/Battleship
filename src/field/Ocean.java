package field;

import ship.Ship;


/**
 * @author Edwin W (570900) on Nov 2020
 * This is the field, where the ships can be placed! It has 2 dimensions and is usually around 11x11 fields large.
 */
public interface Ocean {

    /**
     * The size of one axis, because the field is always square (x*y and x=y)
     *
     * @return the size or length of the field
     */
    int getSize();

    /**
     * Place a part (one coordinate) of a ship at the field.
     * There will be a reference to the ship object. So more than one field contains the same ship object.
     *
     * @param x    the coordinate
     * @param y    the coordinate
     * @param ship the reference to the ship
     */
    void placeShipPart(Ship ship, int x, int y, boolean vertical);
}
