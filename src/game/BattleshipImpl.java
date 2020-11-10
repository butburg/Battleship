package game;

import exceptions.BattleshipException;
import exceptions.StatusException;
import ship.Ship;

import java.awt.*;

/**
 * @author Edwin W (570900) on Nov 2020
 */
public class BattleshipImpl implements Battleship {
    @Override
    public void choosePlayer(String playerName) throws BattleshipException, StatusException {

    }

    @Override
    public boolean setShip(String player, Ship ship) throws BattleshipException, StatusException {
        return false;
    }

    @Override
    public Result attack(String player, Point position) throws BattleshipException, StatusException {
        return null;
    }
}
