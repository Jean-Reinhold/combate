package com.me.combate.models.ItemModel;

public class Item {
    private int x = -1;
    private int y = -1;
    private String team = "user";
    private String pathToIcon;

    public Item(String team) {
        this.team = team;
    }

    public String getTeam() {
        return team;
    }

    protected void setTeam(String team) {
        this.team = team;
    }

    public String getPathToIcon() {
        return pathToIcon;
    }

    protected void setPathToIcon(String pathToIcon) {
        this.pathToIcon = pathToIcon;
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
}
