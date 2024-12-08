package org.thermoweb.aoc.utils;

public class Position {
    private int x;
    private int y;

    Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    @Override
    public String toString() {
        return "(%s,%s)".formatted(this.x, this.y);
    }

    public Position getDirectionTo(Position e) {
        return new Position(this.x - e.getX(), this.y - e.getY());
    }

    public Position getLocationAtOffset(Position p) {
        return new Position(this.x + p.getX(), y + p.getY());
    }

}
