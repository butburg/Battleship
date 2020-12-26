package field;

import exceptions.ExceptionMsg;
import exceptions.OceanException;
import exceptions.ShipException;
import game.Result;
import ship.Ship;

/**
 * @author Edwin W (HTW) on Nov 2020
 * This is an implementation for the ocean. It will be 11x11 squares in size.
 */
public class OceanImpl implements Ocean {

    private final Ship[][] field;

    private final boolean[][] attackedField;
    private final int size;
    private final boolean touchAllowed;
    public OceanImpl(int size, boolean touchAllowed) {
        this.field = new Ship[size][size];
        this.attackedField = new boolean[size][size];
        this.size = size;
        this.touchAllowed = touchAllowed;
    }

    public OceanImpl(int size) {
        this(size, false);
    }

    @Override
    public int getSize() {
        return size;
    }

    @Override
    public void placeShip(Ship ship, int x, int y, boolean vertical) throws OceanException, ShipException {
        if (x < 0 || y < 0 || x >= size || y >= size) throw new OceanException(ExceptionMsg.oc_shipOutside);

        int inc_x = 0;
        int inc_y = 0;

        if (vertical) inc_y = 1;
        else inc_x = 1;

        int shipSize = ship.getModel().returnSize();

        // validate the positions
        checkShipsNotColliding(shipSize, x, y, inc_x, inc_y);
        if (!touchAllowed) checkShipsNotTouching(shipSize, x, y, inc_x, inc_y);

        //finally place the ship into the fields
        placeShipParts(ship, shipSize, x, y, inc_x, inc_y);
    }

    private void checkShipsNotColliding(int shipSize, int x, int y, int inc_x, int inc_y) throws OceanException {
        for (int i = 0; i < shipSize; i++) {
            if (field[x][y] != null) throw new OceanException(ExceptionMsg.oc_shipCollidingPosition);
            x += inc_x;
            y += inc_y;
        }
    }

    private void checkShipsNotTouching(int shipSize, int x, int y, int inc_x, int inc_y) throws OceanException {
        try {
            if (field[x - inc_x][y - inc_y] != null) throw new OceanException(ExceptionMsg.oc_shipTouching);
        } catch (ArrayIndexOutOfBoundsException e) {
        }

        for (int i = 0; i < shipSize; i++) {
            try {
                if (field[x + inc_y][y + inc_x] != null) throw new OceanException(ExceptionMsg.oc_shipTouching);
            } catch (ArrayIndexOutOfBoundsException e) {
            }
            try {
                if (field[x - inc_y][y - inc_x] != null) throw new OceanException(ExceptionMsg.oc_shipTouching);
            } catch (ArrayIndexOutOfBoundsException e) {
            }

            x += inc_x;
            y += inc_y;
        }
        try {
            if (field[x][y] != null) throw new OceanException(ExceptionMsg.oc_shipTouching);
        } catch (ArrayIndexOutOfBoundsException e) {
        }
    }

    private void placeShipParts(Ship ship, int shipSize, int x, int y, int inc_x, int inc_y) throws ShipException {
        for (int i = 0; i < shipSize; i++) {
            field[x][y] = ship;
            ship.locate(i, x, y);
            x += inc_x;
            y += inc_y;
        }
    }

    @Override
    public Result bombAt(Coordinate position) throws OceanException, ShipException {
        int x = position.x;
        int y = position.y;
        Result hitResult = Result.MISSED;
        //check, if the bomb is in the field
        if (x < 0 || y < 0 || x >= size || y >= size) throw new OceanException(ExceptionMsg.oc_attackOutside);
        //check, if the field has been attacked
        if (attackedField[x][y]) throw new OceanException(ExceptionMsg.oc_attackedAlready);
        // store attacked field
        attackedField[x][y] = true;
        // if there is a ship, attack it and get the result
        if (field[x][y] != null) hitResult = attackShipAt(x, y);
        return hitResult;
    }

    private Result attackShipAt(int x, int y) throws ShipException {
        if (field[x][y].hit(x, y)) return Result.SINK;
        return Result.HIT;
    }

    @Override
    public Ship[][] getField() {
        return field;
    }

    @Override
    public boolean[][] getAttackedField() {
        return attackedField;
    }

}
