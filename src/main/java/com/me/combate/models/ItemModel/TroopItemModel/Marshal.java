package com.me.combate.models.ItemModel.TroopItemModel;

import com.me.combate.models.GameBoardModel.GameBoard;
import com.me.combate.models.ItemModel.Item;
import com.me.combate.models.ItemModel.StaticItems.Bomb;
import com.me.combate.models.ItemModel.StaticItems.Flag;

public class Marshal extends Troop {

    public Marshal(String team) {
        super(team);
        this.setLevel(10);
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
        if (gameBoard.getAt(x, y) instanceof Marshal) {
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
}
