package com.me.combate.models.ItemModel.TroopItemModel;

import com.me.combate.models.GameBoardModel.GameBoard;
import com.me.combate.models.ItemModel.Item;

public class Troop extends Item {
    private int level;
    private int x, y;

    public void move(GameBoard gameBoard, int x, int y) {

    }

    public void attack(GameBoard gameBoard, int x, int y) {

    }

    public int getLevel() {
        return level;
    }
}
