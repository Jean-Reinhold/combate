package com.me.combate.models.ItemModel.TroopItemModel;

import com.me.combate.exceptions.IllegalMovement;
import com.me.combate.exceptions.ItemOutOfBounds;
import com.me.combate.models.GameBoardModel.GameBoard;
import com.me.combate.models.ItemModel.StaticItems.Bomb;
import com.me.combate.models.ItemModel.StaticItems.Flag;

import java.util.Random;

public class Soldier extends Troop {
    static private final int level = 2;

    public int attack(GameBoard gameBoard, int x, int y){
        validateAttack(gameBoard, x, y);

        if (gameBoard.getAt(x, y) instanceof Bomb){
            gameBoard.removeAt(this, getX(), getY());
            return -1;
        }
        if (gameBoard.getAt(x, y) instanceof Flag){
            return 0;
        }
        if (gameBoard.getAt(x, y) instanceof Soldier){
            Random random = new Random();
            boolean iWin = random.nextBoolean();
            if (iWin){
                gameBoard.removeAt(gameBoard.getAt(x, y), x, y);
                return 1;
            } else {
                gameBoard.removeAt(this, getX(), getY());
                return -1;
            }
        }
        Troop opponent = (Troop) gameBoard.getAt(x, y);
        if (getLevel() < opponent.getLevel()){
            gameBoard.removeAt(this, getX(), getY());
            return -1;
        }
        return 1;
    }

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
