package com.me.combate.models.ItemModel.TroopItemModel;

public class TroopFactory {
    public static Troop createTroop(String type) {
        Troop troop;
        switch (type.toLowerCase()) {
            case "general":
                troop = new General();
                break;
            case "soldier":
                troop = new Soldier();
                break;
            case "spy":
                troop = new Spy();
                break;
            case "gunsmith":
                troop = new Gunsmith();
                break;
            default:
                throw new IllegalArgumentException("Invalid troop type: " + type);
        }
        return troop;
    }
}

