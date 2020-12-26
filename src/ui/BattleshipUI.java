package ui;

import game.BattleshipImpl;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;

/**
 * @author Edwin W (HTW) on Dez 2020
 */



public class BattleshipUI {
    private final String playerName;
    private final PrintStream outStream;
    private final BufferedReader inBufferReader;
    private final BattleshipImpl gameEngine;

    public BattleshipUI(String playerName, PrintStream os, InputStream in) {
        this.playerName = playerName;
        this.outStream = os;
        this.inBufferReader = new BufferedReader(new InputStreamReader(in));

        this.gameEngine = new BattleshipImpl(playerName);
    }

    public void printUsage() {
    }

    public void runLoop() {
    }
}
