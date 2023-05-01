package com.me.combate.models.GameBoardModel;

import com.me.combate.exceptions.ItemOutOfBounds;
import com.me.combate.models.ItemModel.Item;

public class GameBoard {
    private final int GAMEBOARD_SIZE = 5;
    private final Item[][] board;
    private boolean debug = false;
    private String whoIsPlaying = "user";

    public GameBoard() {
        this.board = new Item[getGameBoardSize()][getGameBoardSize()];
        this.initializeBoard();

    }
    private void initializeBoard(){
        for (int i = 0; i < this.getGameBoardSize(); i++){
            for (int j = 0; j < this.getGameBoardSize(); j++){
                this.board[i][j] = null;
            }
        }
    }
    public void insertItem(Item item, int x, int y){
        if (x >= this.getGameBoardSize()){
            throw new ItemOutOfBounds("Cannot position item on x = " + x);
        }
        if (y >= this.getGameBoardSize()){
            throw new ItemOutOfBounds("Cannot position item on y = " + y);
        }

        this.board[x][y] = item;

        item.setX(x);
        item.setY(y);
    }

    public void removeItem(Item item, int x, int y){
        if (x >= this.getGameBoardSize()){
            throw new ItemOutOfBounds("Cannot position item on x = " + x);
        }
        if (y >= this.getGameBoardSize()){
            throw new ItemOutOfBounds("Cannot position item on y = " + y);
        }

        this.board[x][y] = null;
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
