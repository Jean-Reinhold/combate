package com.me.combate.models.GameBoardModel;

import com.me.combate.exceptions.IllegalMovement;
import com.me.combate.exceptions.ItemOutOfBounds;
import com.me.combate.models.ItemModel.Item;
import com.me.combate.models.ItemModel.TroopItemModel.Troop;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class GameBoard {
    private final int GAMEBOARD_SIZE = 5;
    private final Item[][] board;
    private int lakeRow = 2;
    private int lakeCol = 2;
    private boolean debug = false;
    private String whoIsPlaying = "user";

    public GameBoard() {
        board = new Item[getGameBoardSize()][getGameBoardSize()];
        initializeBoard();
    }

    public Item getAt(int x, int y) {
        return this.board[x][y];
    }

    public void setAt(int x, int y, Item value) {
        this.board[x][y] = value;
    }

    private void initializeBoard() {
        for (int i = 0; i < this.getGameBoardSize(); i++) {
            for (int j = 0; j < this.getGameBoardSize(); j++) {
                setAt(i, j, null);
            }
        }
    }

    public void insertItem(Item item, int x, int y) throws ItemOutOfBounds {
        if (x >= this.getGameBoardSize()) {
            throw new ItemOutOfBounds("Cannot position item on x = " + x);
        }
        if (y >= this.getGameBoardSize()) {
            throw new ItemOutOfBounds("Cannot position item on y = " + y);
        }

        setAt(x, y, item);

        if (item != null) {
            item.setX(x);
            item.setY(y);
        }
    }

    public void removeAt(Item item, int x, int y) {
        if (x >= this.getGameBoardSize()) {
            throw new ItemOutOfBounds("Cannot position item on x = " + x);
        }
        if (y >= this.getGameBoardSize()) {
            throw new ItemOutOfBounds("Cannot position item on y = " + y);
        }

        setAt(x, y, null);
        item.setX(-1);
        item.setY(-1);
    }

    public int getGameBoardSize() {
        return GAMEBOARD_SIZE;
    }

    public boolean isDebugging() {
        return debug;
    }

    public void setIsDebugging(boolean debug) {
        this.debug = debug;
    }

    public String getWhoIsPlaying() {
        return whoIsPlaying;
    }

    public void setWhoIsPlaying(String whoIsPlaying) {
        this.whoIsPlaying = whoIsPlaying;
    }

    public int getLakeRow() {
        return lakeRow;
    }

    public void setLakeRow(int lakeRow) {
        this.lakeRow = lakeRow;
    }

    public int getLakeCol() {
        return lakeCol;
    }

    public void setLakeCol(int lakeCol) {
        this.lakeCol = lakeCol;
    }

    public HashMap<String, Integer> itemCount(String team) {
        HashMap<String, Integer> counter = new HashMap();
        initializeCounter(counter);

        for (int i = 0; i < getGameBoardSize(); i++) {
            for (int j = 0; j < getGameBoardSize(); j++) {
                Item piece = getAt(i, j);
                if (piece == null || !piece.getTeam().equals(team))
                    continue;

                String itemType = piece.getSubClass();
                try {
                    counter.put(itemType, counter.get(itemType) + 1);
                } catch (NullPointerException npe) {
                }
            }
        }
        return counter;
    }

    private void initializeCounter(HashMap<String, Integer> counter) {
        Set<String> itemTypes = Set.of("marshal", "gunsmith", "soldier", "spy", "bomb", "flag");

        for (String itemType : itemTypes) {
            counter.put(itemType, 0);
        }
    }

    public ArrayList<Item> getMachineItems() {
        ArrayList<Item> machineItems = new ArrayList<Item>();
        for (Item[] row : board) {
            for (Item item : row) {
                if (item != null && item.getTeam().equals("machine")) {
                    machineItems.add(item);
                }
            }
        }

        return machineItems;
    }

    public ArrayList<Troop> getMachineTroops() {
        ArrayList<Troop> machineTroops = new ArrayList<Troop>();
        for (Item[] row : board) {
            for (Item item : row) {
                if (item instanceof Troop && item.getTeam().equals("machine")) {
                    machineTroops.add((Troop) item);
                }
            }
        }

        return machineTroops;
    }

    public ArrayList<ArrayList<Integer>> getTroopPossibleAttacks(Troop troop) {
        ArrayList<ArrayList<Integer>> attacks = new ArrayList<ArrayList<Integer>>();

        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if (i == 0 && j == 0)
                    continue;
                try {
                    troop.validateAttack(this, troop.getX() - i, troop.getY() - j);

                    ArrayList<Integer> coordinates = new ArrayList<Integer>();
                    coordinates.add(troop.getX() - i);
                    coordinates.add(troop.getY() - j);
                    attacks.add(coordinates);
                } catch (ItemOutOfBounds | IllegalMovement e) {
                    continue;
                }
            }
        }

        return attacks;
    }

    public ArrayList<ArrayList<Integer>> getTroopPossibleMoves(Troop troop) {
        ArrayList<ArrayList<Integer>> moves = new ArrayList<ArrayList<Integer>>();

        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if (i == 0 && j == 0)
                    continue;
                try {
                    troop.validateMove(this, troop.getX() - i, troop.getY() - j);

                    ArrayList<Integer> coordinates = new ArrayList<Integer>();
                    coordinates.add(troop.getX() - i);
                    coordinates.add(troop.getY() - j);
                    moves.add(coordinates);
                } catch (ItemOutOfBounds | IllegalMovement e) {
                    continue;
                }
            }
        }

        return moves;
    }

}
