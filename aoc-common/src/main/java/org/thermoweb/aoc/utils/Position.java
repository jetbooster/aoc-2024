package org.thermoweb.aoc.utils;

import java.util.Objects;

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
        return new Position(e.getX() - this.x, e.getY() - this.y);
    }

    public Position getLocationAtOffset(Position p) {
        return new Position(this.x + p.getX(), y + p.getY());
    }

    public Position multiplyBy(int value) {
        return new Position(this.x * value, this.y * value);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof Position)) {
            return false;
        }
        Position p = (Position) obj;
        return this.getX() == p.getX() && this.getY() == this.getY();
    }

    @Override
    public int hashCode() {
        // TODO Auto-generated method stub
        return Objects.hash(x, y);
    }

}
