package Logic;

/**
 * A data class holding two values.<br>
 * Has equals().
 */
public class Pair {
    private int x;
    private int y;

    // region get and set
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
    // endregion

    public Pair(int x, int y) {
        this.x = x;
        this.y = y;
    }

    // region equals and hashcode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Pair pair = (Pair) o;

        if (x != pair.x) return false;
        return y == pair.y;
    }

    @Override
    public int hashCode() {
        return 31 * x + y;
    }
    // endregion
}
