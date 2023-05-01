package com.me.combate.models.ItemModel.TroopItemModel;

import com.me.combate.exceptions.IllegalMoviment;
import com.me.combate.exceptions.ItemOutOfBounds;
import com.me.combate.models.GameBoardModel.GameBoard;
import com.me.combate.models.ItemModel.Item;

public class Troop extends Item {
    private int level;
    private int x, y;

    public void move(GameBoard gameBoard, int x, int y) throws ItemOutOfBounds, IllegalMoviment {
        this.validateMove(gameBoard, x, y);
    }

    protected void validateMove(GameBoard gameBoard, int x, int y) {
        if (x < 0) {
            throw new ItemOutOfBounds("New x cannot be negative");
        }
        if (y < 0) {
            throw new ItemOutOfBounds("New y cannot be negative");
        }
        if (x >= gameBoard.getGameBoardSize()) {
            throw new ItemOutOfBounds("New x cannot be greater than or equal to the board size");
        }
        if (y >= gameBoard.getGameBoardSize()) {
            throw new ItemOutOfBounds("New y cannot be greater than or equal to the board size");
        }
        if (Math.abs(y - this.y) > 1) {
            throw new IllegalMoviment("Cannot go this far on y axis");
        }
        if (Math.abs(x - this.x) > 1) {
            throw new IllegalMoviment("Cannot go this far on x axis");
        }
        if (gameBoard.getAt(x, y) != null) {
            throw new IllegalMoviment("Cannot move to an occupied position");
        }
    }

    public int getLevel() {
        return level;
    }
}
