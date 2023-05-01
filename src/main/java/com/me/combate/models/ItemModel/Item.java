package com.me.combate.models.ItemModel;

public class Item {
    private int x, y;
    private String team = "user";
    private String path;

    public String getTeam() {
        return team;
    }

    protected void setTeam(String team) {
        this.team = team;
    }

    public String getPath() {
        return path;
    }

    protected void setPath(String path) {
        this.path = path;
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
