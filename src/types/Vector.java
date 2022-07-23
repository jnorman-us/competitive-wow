package types;

import java.io.Serializable;

public class Vector implements Serializable {
    private int x, y;

    public Vector() {
        x = 0;
        y = 0;
    }

    public Vector(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Vector(Bounds bounds) {
        this.x = (int) (Math.random() * bounds.high().X() + 0);
        this.y = (int) (Math.random() * bounds.high().Y() + 0);
    }

    public Vector(Vector c) {
        this.x = c.x;
        this.y = c.y;
    }

    public void set(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void copy(Vector o) {
        this.x = o.x;
        this.y = o.y;
    }

    public int X() {
        return x;
    }

    public int Y() {
        return y;
    }

    public void move(Direction d) {
        if(d == Direction.UP) {
            y --;
        } else if(d == Direction.DOWN) {
            y ++;
        } else if(d == Direction.LEFT) {
            x --;
        } else if(d == Direction.RIGHT) {
            x ++;
        }
    }

    public void enforceBounds(Bounds b) {
        x = Math.max(0, Math.min(b.high().X(), x));
        y = Math.max(0, Math.min(b.high().Y(), y));
    }

    public boolean checkAdjacent(Vector o) {
        if(x == o.x && y == o.y - 1) return true;
        if(x == o.x && y == o.y + 1) return true;
        if(y == o.y && x == o.x - 1) return true;
        if(y == o.y && x == o.x + 1) return true;
        return false;
    }

    public boolean equals(Vector o) {
        return x == o.x && y == o.y;
    }

    @Override
    public int hashCode() {
        return new String(x + "," + y).hashCode();
    }

    @Override
    public String toString() {
        return "(" + x + ", " + y + ")";
    }
}
