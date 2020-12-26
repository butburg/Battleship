package main;

import ui.BattleshipUI;

public class Main {

    public static void main(String[] args) {
        System.out.println("Battleship 2020 by Edwin W");

        if (args.length < 1) {
            System.err.println("Need a player name as parameter! Please restart with player name!");
            System.exit(1);
        }

        System.out.println("Have fun " + args[0] + "!");
        BattleshipUI userCmd = new BattleshipUI(args[0], System.out, System.in);

        userCmd.printUsage();
        userCmd.runLoop();
    }
}
