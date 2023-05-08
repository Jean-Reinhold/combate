package com.me.combate.models.ItemModel;

import com.me.combate.models.ItemModel.StaticItems.Bomb;
import com.me.combate.models.ItemModel.StaticItems.Flag;
import com.me.combate.models.ItemModel.TroopItemModel.Gunsmith;
import com.me.combate.models.ItemModel.TroopItemModel.Marshal;
import com.me.combate.models.ItemModel.TroopItemModel.Soldier;
import com.me.combate.models.ItemModel.TroopItemModel.Spy;

public class ItemFactory {
    public static Item createPiece(String type, String team) {
        Item piece;
        String className = type;

        try {
            className = className.toLowerCase();
        } catch (Exception e) {
            return null;
        }

        switch (className) {
            case "marshal":
                piece = new Marshal(team);
                break;
            case "soldier":
                piece = new Soldier(team);
                break;
            case "spy":
                piece = new Spy(team);
                break;
            case "gunsmith":
                piece = new Gunsmith(team);
                break;
            case "bomb":
                piece = new Bomb(team);
                break;
            case "flag":
                piece = new Flag(team);
                break;
            default:
                throw new IllegalArgumentException("Invalid piece type: " + type);
        }
        return piece;
    }
}

