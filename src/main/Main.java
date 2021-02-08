package main;

import ui.BattleshipUI;

public class Main {

    public static void main(String[] args) {
        System.out.println("##########################");
        System.out.println("BATTLESHIP 2020 by Edwin W");
        System.out.println("##########################");
        if (args.length < 1) {
            System.err.println("Need a player name as parameter! Please restart with player name!");
            System.exit(1);
        }


        System.out.println("Please connect with another player by typing the \n" +
                " command open to open the port. The second player will be able \n" +
                " to connect to this open port with connect and your hostname in\n" +
                " the network. When the connection is established, both can set\n" +
                " their ships and start to play.");
        System.out.println("Have fun " + args[0] + "!");
        BattleshipUI userCmd = new BattleshipUI(args[0], System.out, System.in);

        userCmd.printUsage();
        userCmd.runLoop();
    }
}
