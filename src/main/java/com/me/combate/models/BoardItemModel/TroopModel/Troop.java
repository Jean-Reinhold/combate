package com.me.combate.models.BoardItemModel.TroopModel;

import com.me.combate.models.BoardItemModel.Item;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;

public class Troop extends Item {
    @FXML
    private Button bt;
    private int level;
    private int x, y;

    public void move(GridPane table, int x, int y){

    }
    public void attack(GridPane table, int x, int y){

    }

    public int getLevel() {
        return level;
    }

    public int getX() {
        return x;
    }

    protected void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    protected void setY(int y) {
        this.y = y;
    }
}
