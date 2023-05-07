package com.me.combate.models.GameBoardModel;

import com.me.combate.exceptions.ItemOutOfBounds;
import com.me.combate.models.ItemModel.Item;

import java.util.HashMap;
import java.util.Set;

public class GameBoard {
    private final int GAMEBOARD_SIZE = 5;
    private final Item[][] board;
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

    public void insertItem(Item item, int x, int y) {
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
}
