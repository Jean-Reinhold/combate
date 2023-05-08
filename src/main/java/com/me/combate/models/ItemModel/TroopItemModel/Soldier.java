package com.me.combate.models.ItemModel.TroopItemModel;

import com.me.combate.exceptions.IllegalMovement;
import com.me.combate.exceptions.ItemOutOfBounds;
import com.me.combate.models.GameBoardModel.GameBoard;
import com.me.combate.models.ItemModel.Item;
import com.me.combate.models.ItemModel.StaticItems.Bomb;
import com.me.combate.models.ItemModel.StaticItems.Flag;

public class Soldier extends Troop {
    public Soldier(String team) {
        super(team);
        this.setLevel(2);
    }

    public AttackResult attack(GameBoard gameBoard, int x, int y) {
        validateAttack(gameBoard, x, y);

        Item piece = gameBoard.getAt(x, y);

        if (gameBoard.getAt(x, y) instanceof Bomb) {
            gameBoard.removeAt(this, getX(), getY());
            return AttackResult.LOST;
        }
        if (gameBoard.getAt(x, y) instanceof Flag) {
            return AttackResult.FINISHED_GAME;
        }
        if (gameBoard.getAt(x, y) instanceof Soldier) {
            gameBoard.removeAt(piece, x, y);
            gameBoard.removeAt(this, getX(), getY());

            return AttackResult.DRAW;
        }

        Troop opponent = (Troop) gameBoard.getAt(x, y);
        if (getLevel() < opponent.getLevel()) {
            gameBoard.removeAt(this, getX(), getY());
            return AttackResult.LOST;
        } else {
            gameBoard.removeAt(this, getX(), getY());
            gameBoard.insertItem(this, x, y);
            return AttackResult.WON;
        }
    }

    protected boolean run(GameBoard gameBoard, int x, int y, int directionX, int directionY) {
        int posX = getX();
        int posY = getY();

        while (posX != x || posY != y) {
            posX += directionX;
            posY += directionY;

            Item pieceOnPath = gameBoard.getAt(posX, posY);

            if (pieceOnPath != null)
                return true;
        }

        return false;
    }

    @Override
    public void validateMove(GameBoard gameBoard, int x, int y) {
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
//        if (Math.abs(y - getY()) > 1) {
//            throw new IllegalMovement("Cannot go this far on y axis");
//        }
//        if (Math.abs(x - getX()) > 1) {
//            throw new IllegalMovement("Cannot go this far on x axis");
//        }
        if (Math.abs(x - getX()) > 1) {
            if (Math.abs(y - getY()) != 0 && (Math.abs(y - getY()) != Math.abs(x - getX()))) {
                throw new IllegalMovement("Invalid movement pattern");
            }
        }

        if (Math.abs(y - getY()) > 1) {
            if (Math.abs(x - getX()) != 0 && (Math.abs(y - getY()) != Math.abs(x - getX()))) {
                throw new IllegalMovement("Invalid movement pattern");
            }
        }

        if (run(gameBoard, x, y, (int) Math.signum(x - getX()), (int) Math.signum(y - getY()))) {
            throw new IllegalMovement("Cannot move through an occupied position");
        }
//        if (Math.abs(y - getY()) > 1) {
//            if ((this.getX() - x) != 0) {
//                throw new IllegalMovement("Cannot go this far in both directions");
//            }
//        }
        if (gameBoard.getAt(x, y) != null) {
            throw new IllegalMovement("Cannot move to an occupied position");
        }
    }
}
