package com.me.combate.models.ItemModel.TroopItemModel;

import com.me.combate.models.ItemModel.Item;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;

public class Troop extends Item {
    @FXML
    private Button bt;
    private int level;

    public void move(GridPane table, int x, int y) {

    }

    public void attack(GridPane table, int x, int y) {

    }

    public int getLevel() {
        return level;
    }
}
