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
    private int size;

    public OceanImpl(int size) {
        this.field = new Ship[size][size];
        this.size = size;
    }

    @Override
    public int getSize() {
        return size;
    }

    @Override
    public void placeShipPart(Ship ship, int x, int y, boolean vertical) throws OceanException, ShipException {
        if (x < 0 || y < 0 || x >= size || y >= size) throw new OceanException(ExceptionMsg.oc_shipOutside);

        int inc_x = 0;
        int inc_y = 0;

        if (vertical) inc_y = 1;
        else inc_x = 1;

        int shipSize = ship.getModel().returnSize();

        for (int i = 0; i < shipSize; i++) {
            if (field[x][y] != null) throw new OceanException(ExceptionMsg.oc_shipCollidingPosition);
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
        if (x < 0 || y < 0 || x >= size || y >= size) throw new OceanException(ExceptionMsg.oc_attackOutside);
        if (field[x][y] != null) hitResult = attackShipAt(x, y);
        return hitResult;
    }

    private Result attackShipAt(int x, int y) throws ShipException {
        if (field[x][y].hit(x, y)) return Result.SINK;
        return Result.HIT;
    }


}
