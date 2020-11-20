package main;

import ship.Ship;
import ship.ShipImpl;
import ship.Shipmodel;

import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {
        // write your code here

        //some adhoc tests ...
        ArrayList<Ship> ships1 = new ArrayList<>();
        ships1.add(new ShipImpl(Shipmodel.CRUISERS));
        Ship looo = new ShipImpl(Shipmodel.BATTLESHIP);
        ships1.add(looo);
        System.out.println(ships1);
        Ship shipToSet = null;
        for (Ship ship : ships1) {
            if (ship.getModel() == Shipmodel.BATTLESHIP)
                shipToSet = ship;
        }
        System.out.println("1" + shipToSet);
        System.out.println("2" + looo);
        System.out.println(ships1.indexOf(new ShipImpl(Shipmodel.BATTLESHIP)));
        ships1.remove(new ShipImpl(Shipmodel.BATTLESHIP));
        System.out.println(ships1);
    }
}
