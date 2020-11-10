package game;

import exceptions.BattleshipException;
import exceptions.PhaseException;
import ship.Ship;

import java.awt.*;

/**
 * @author Edwin W (570900) on Nov 2020
 */
public class BattleshipImpl implements Battleship {
    @Override
    public void choosePlayer(String playerName) throws BattleshipException, PhaseException {

    }

    @Override
    public boolean setShip(String player, Ship ship) throws BattleshipException, PhaseException {
        return false;
    }

    @Override
    public Result attack(String player, Point position) throws BattleshipException, PhaseException {
        return null;
    }
}
