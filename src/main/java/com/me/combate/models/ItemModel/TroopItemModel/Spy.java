package com.me.combate.models.ItemModel.TroopItemModel;

import com.me.combate.models.GameBoardModel.GameBoard;
import com.me.combate.models.ItemModel.StaticItems.Bomb;
import com.me.combate.models.ItemModel.StaticItems.Flag;

import java.util.Random;

public class Spy extends Troop {
    static private final int level = 1;

    public int attack(GameBoard gameBoard, int x, int y){
        validateAttack(gameBoard, x, y);

        if (gameBoard.getAt(x, y) instanceof General){
            gameBoard.removeAt(gameBoard.getAt(x, y), x, y);
            return 1;
        }
        if (gameBoard.getAt(x, y) instanceof Bomb){
            gameBoard.removeAt(this, getX(), getY());
            return -1;
        }
        if (gameBoard.getAt(x, y) instanceof Flag){
            return 0;
        }
        if (gameBoard.getAt(x, y) instanceof Spy){
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
}
