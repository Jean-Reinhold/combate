package com.me.combate.models.BoardItemModel;

public class Item {
    private int x, y;
    private String team;
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
