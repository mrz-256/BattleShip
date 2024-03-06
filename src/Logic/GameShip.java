package Logic;


/**
 * <h1>Game Ship class</h1>
 * <p>A data class representing the game ship.</p>
 * <p>Makes it easy to check if ship was sunk completely without having to check whole board.</p>
 */
public class GameShip {
    private final int[][] body_parts_positions;
    private final int[] live_parts;
    private final int[] start_and_end;

    public int[] getStart_and_end() {
        return start_and_end;
    }

    public int getSize(){
        return live_parts.length;
    }

    public GameShip(int x, int y, int x1, int y1) {
        int ship_size = Math.max(x1 - x, y1 - y);

        start_and_end = new int[]{x, y, x1, y1};
        body_parts_positions = new int[ship_size][2];
        live_parts = new int[ship_size];

        if (x1 - x > y1 - y) {
            for (int i = 0; i < x1 - x; i++) {
                body_parts_positions[i][0] = x + i;
                body_parts_positions[i][1] = y;
            }
        } else {
            for (int i = 0; i < y1 - y; i++) {
                body_parts_positions[i][0] = x;
                body_parts_positions[i][1] = y + i;
            }
        }
    }

    /**
     * Lets user/enemy know if they sunk this particular ship.
     *
     * @return true if ship is dead, false otherwise
     */
    public boolean tryShoot(int x, int y) {
        int sum = 0;
        for (int i = 0; i < live_parts.length; i++) {
            if (body_parts_positions[i][0] == x && body_parts_positions[i][1] == y) {
                live_parts[i] = 1;
            }
            sum += live_parts[i];
        }
        return sum == live_parts.length;
    }

    @Override
    public String toString() {
        // used only for debugging
        StringBuilder ship = new StringBuilder();
        for (int[] position : body_parts_positions) {
            ship.append("[").append(position[0]).append(", ").append(position[1]).append("], ");
        }
        return ship.toString();

    }
}
