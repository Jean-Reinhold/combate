package com.me.combate.models.GameBoardModel;

import com.me.combate.exceptions.ItemOutOfBounds;
import com.me.combate.models.ItemModel.Item;

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

        item.setX(x);
        item.setY(y);
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
}
