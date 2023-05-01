package com.me.combate.models.ItemModel.TroopItemModel;

public class TroopFactory {
    public static Troop createTroop(String type, String team) {
        Troop troop;
        switch (type.toLowerCase()) {
            case "general":
                troop = new General(team);
                break;
            case "soldier":
                troop = new Soldier(team);
                break;
            case "spy":
                troop = new Spy(team);
                break;
            case "gunsmith":
                troop = new Gunsmith(team);
                break;
            default:
                throw new IllegalArgumentException("Invalid troop type: " + type);
        }
        return troop;
    }
}

