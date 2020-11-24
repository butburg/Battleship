package ship;

import exceptions.ShipException;

import java.awt.*;

/**
 * @author Edwin W (570900) on Nov 2020
 * The ship consists of at least two parts that must be hit with an attack in order for it to sink.
 * So the size matters. The position is also stored by the ship.
 * <p>
 * The size is in relation with the type of ship, which stored in enums.
 */
public interface Ship {

    /**
     * @param field
     * @param located_x
     * @param located_y
     */
    void locate(int field, int located_x, int located_y);

    /**
     * Attack the ship on a specific field/part. Destroys the selected healthy field.
     * A 5 field ship can get destroyed at field 4 but not 5!
     *
     * @param field that should be destroyed
     * @return true if the ship sinks. false if it remains
     * @throws ShipException when the field is not in the size of the ship
     */
    boolean hit(int field) throws ShipException;

    /**
     * Attack the ship at a specific position in the field.
     *
     * @param x coordinate
     * @param y coordinate
     * @return true if the ship sinks. false if it remains
     * @throws ShipException when the ship is not positioned at the coordinate
     */
    boolean hit(int x, int y) throws ShipException;

    /**
     * Check, if the ship is sunk!
     *
     * @return true if the ship is destroyed, sunk. false if it is intact
     */
    boolean sunk();

    /**
     * Get the Anchor coordinate
     *
     * @return the anchor as a new Point(x,y)
     */
    Point getAnchor();

    /**
     * Returns the total number of fields, i.e. the length, the size of the ship.
     *
     * @return the number of fields (healthy and destroyed)
     */
    int getSize();

    /**
     * The positions as array stored in the ship.
     *
     * @return the positions as array
     */
    Point[] getPosition();

    /**
     * Get the type of ship model.
     *
     * @return the ship model
     */
    Shipmodel getModel();

}
