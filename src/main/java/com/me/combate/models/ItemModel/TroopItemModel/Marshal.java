package com.me.combate.models.ItemModel.TroopItemModel;

import com.me.combate.models.GameBoardModel.GameBoard;
import com.me.combate.models.ItemModel.Item;
import com.me.combate.models.ItemModel.StaticItems.Bomb;
import com.me.combate.models.ItemModel.StaticItems.Flag;

import java.util.Random;

public class Marshal extends Troop {

    public Marshal(String team) {
        super(team);
        this.setLevel(10);
    }

    public int attack(GameBoard gameBoard, int x, int y) {
        validateAttack(gameBoard, x, y);

        Item piece = gameBoard.getAt(x, y);
        if (piece == null)
            return -2;
        
        if (gameBoard.getAt(x, y) instanceof Bomb) {
            gameBoard.removeAt(this, getX(), getY());
            return -1;
        }
        if (gameBoard.getAt(x, y) instanceof Flag) {
            return 0;
        }
        if (gameBoard.getAt(x, y) instanceof Marshal) {
            gameBoard.removeAt(piece, x, y);
            gameBoard.removeAt(this, getX(), getY());
            
            return -3;
        }
        Troop opponent = (Troop) gameBoard.getAt(x, y);
        
        if (getLevel() < opponent.getLevel()) {
            gameBoard.removeAt(this, getX(), getY());
            return -1;
        } else {
            gameBoard.removeAt(this, getX(), getY());
            gameBoard.insertItem(this,x, y);
            return 1;
        }
    }
}
