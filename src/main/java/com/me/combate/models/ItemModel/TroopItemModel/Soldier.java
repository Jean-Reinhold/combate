package com.me.combate.models.ItemModel.TroopItemModel;

import com.me.combate.exceptions.IllegalMovement;
import com.me.combate.exceptions.ItemOutOfBounds;
import com.me.combate.models.GameBoardModel.GameBoard;

public class Soldier extends Troop {
    static private final int level = 2;

    @Override
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
        if (Math.abs(y - getY()) > 1) {
            throw new IllegalMovement("Cannot go this far on y axis");
        }
        if (Math.abs(x - getX()) > 1) {
            throw new IllegalMovement("Cannot go this far on x axis");
        }
        if (Math.abs(x - getX()) > 1) {
            if ((getY() - y) != 0) {
                throw new IllegalMovement("Cannot go this far in both directions");
            }
        }
        if (Math.abs(y - getY()) > 1) {
            if ((this.getX() - x) != 0) {
                throw new IllegalMovement("Cannot go this far in both directions");
            }
        }
        if (gameBoard.getAt(x, y) != null) {
            throw new IllegalMovement("Cannot move to an occupied position");
        }
    }
}
