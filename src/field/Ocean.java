package field;

import exceptions.OceanException;
import exceptions.ShipException;
import game.Result;
import ship.Ship;


/**
 * @author Edwin W (HTW) on Nov 2020
 * This is the field, where the ships can be placed! It has 2 dimensions and is usually around 11x11 fields large.
 */
public interface Ocean {

    /**
     * The size of one axis, because the field is always a square (x*y and x=y)
     *
     * @return the size or length of the field
     */
    int getSize();

    /**
     * Place a part (one coordinate) of a ship at the field.
     * There will be a reference to the ship object. So more than one field contains the same ship object.
     *
     * @param ship     the reference to the ship
     * @param x        the coordinate
     * @param y        the coordinate
     * @param vertical if the Ship should be placed from left to right(horizontal-false) or from top to bottom(vertical-true)
     * @throws OceanException when the position is not ok and the ship can't be placed
     * @throws ShipException  when the ship recognizes a bad placing
     */
    void placeShip(Ship ship, int x, int y, boolean vertical) throws OceanException, ShipException;

    /**
     * This will attack a field in the ocean. If at the position is a ship it will be attacked.
     * The Result will be Missed or the result from the attacked ship. If the ship got its last hit it will
     * return Sunk than Hit.
     *
     * @param position the field in the ocean, that should be bombed, attacked
     * @return Missed-no ship, Hit-hit a part of a ship, Sunk-hit a ship and it has no healthy parts left
     * @throws OceanException when the attack is not in the field
     * @throws ShipException  when the ship can't find its part coordination that should be attacked. Shouldn't be the case.
     */
    Result bombAt(Coordinate position) throws OceanException, ShipException;
}
