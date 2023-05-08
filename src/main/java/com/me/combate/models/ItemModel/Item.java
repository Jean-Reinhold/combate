package com.me.combate.models.ItemModel;

public class Item {
    private int x = -1;
    private int y = -1;
    private String team = "user";
    private boolean visibility = true;

    public Item(String team) {
        this.team = team;
    }

    public boolean getVisibility() {
        return visibility;
    }

    public void setVisibility(boolean visibility) {
        this.visibility = visibility;
    }

    public String getTeam() {
        return team;
    }

    protected void setTeam(String team) {
        this.team = team;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public String getSubClass() {
        String className = this.getClass().getName();
        int startSubClassIndex = className.lastIndexOf('.') + 1;

        return className.substring(startSubClassIndex).toLowerCase();
    }
}
