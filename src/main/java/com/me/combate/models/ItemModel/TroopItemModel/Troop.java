package com.me.combate.models.ItemModel.TroopItemModel;

import com.me.combate.exceptions.IllegalMovement;
import com.me.combate.exceptions.ItemOutOfBounds;
import com.me.combate.models.GameBoardModel.GameBoard;
import com.me.combate.models.ItemModel.Item;

public class Troop extends Item {
    protected int level;
    private int x, y;

    public Troop(String team) {
        super(team);
    }

    public AttackResult attack(GameBoard gameBoard, int x, int y) {
        return AttackResult.DRAW;
    }

    public void move(GameBoard gameBoard, int x, int y) throws ItemOutOfBounds, IllegalMovement {
        validateMove(gameBoard, x, y);

        Item targetItem = gameBoard.getAt(x, y);
        int originalX = getX();
        int originalY = getY();

        gameBoard.setAt(x, y, this);
        gameBoard.setAt(originalX, originalY, targetItem);

        setX(x);
        setY(y);
    }

    protected void validateMove(GameBoard gameBoard, int x, int y) throws ItemOutOfBounds, IllegalMovement {
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
        if (gameBoard.getAt(x, y) != null) {
            throw new IllegalMovement("Cannot move to an occupied position");
        }
    }

    protected void validateAttack(GameBoard gameBoard, int x, int y) throws ItemOutOfBounds, IllegalMovement {
        if (x < 0) {
            throw new ItemOutOfBounds("Target x cannot be negative");
        }
        if (y < 0) {
            throw new ItemOutOfBounds("Target y cannot be negative");
        }
        if (x >= gameBoard.getGameBoardSize()) {
            throw new ItemOutOfBounds("Target x cannot be greater than or equal to the board size");
        }
        if (y >= gameBoard.getGameBoardSize()) {
            throw new ItemOutOfBounds("Target y cannot be greater than or equal to the board size");
        }
        if (Math.abs(y - getY()) > 1 || Math.abs(x - getX()) > 1) {
            throw new IllegalMovement("Cannot attack to a target that is not adjacent");
        }
        if (gameBoard.getAt(x, y) == null) {
            throw new IllegalMovement("Cannot attack an empty position");
        }
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public enum AttackResult {
        LOST, WON, DRAW, FINISHED_GAME
    }
}
