package Logic;


import java.util.*;


/**
 * <h1>Game Enemy Logic</h1>
 * The enemy 'bot' chooses the next move here.
 */
public class GameEnemyLogic {
    private int[][] probability_board;
    private final Set<Pair> ignored;


    public GameEnemyLogic() {
        this.probability_board = new int[10][10];
        this.ignored = new HashSet<>();
    }

    /**
     * Used to get the enemy's next move.
     *
     * @param player_board the current board with players ships on it.
     * @param player_ships the current ships player has.
     * @return the next position in form of [x,y] where enemy will strike.
     */
    public int[] getNext(int[][] player_board, Set<GameShip> player_ships) {
        int[] position = huntFoundShots(player_board); //sets mean length of game to 97 from 90, useless
        if (position != null) return position;

        updateProbabilityBoard(player_board, player_ships);
        position = findMaxChance();
        ignored.add(new Pair(position[0], position[1]));

        return position;
    }

    /**
     * @return the position with the highest probability in form of [x, y] array.
     */
    private int[] findMaxChance() {
        Set<int[]> max = new HashSet<>();
        max.add(new int[2]);
        int last_max = 0;

        for (int y = 0; y < 10; y++) {
            for (int x = 0; x < 10; x++) {
                if (probability_board[y][x] > last_max) {
                    last_max = probability_board[y][x];
                    max.clear();
                    max.add(new int[]{x, y});
                } else if (probability_board[y][x] == last_max) {
                    max.add(new int[]{x, y});
                }
            }
        }
        // for harder predictability randomness
        Iterator<int[]> iterator = max.iterator();
        int index = new Random().nextInt(max.size());
        for (int i = 0; i < index - 1; i++) {
            iterator.next();
        }

        return iterator.next();
    }

    /**
     * Updates/remakes the probability board.
     *
     * @param player_board the current board with players ships on it.
     * @param ships        the current ships player has.
     */
    private void updateProbabilityBoard(int[][] player_board, Set<GameShip> ships) {
        // [1] . . . Goes for all possible ships in all possible orientations (horizontal and vertical).
        // [2] . . . Goes for all 'heads' of the ships from [1] which can fit on board.
        // [3] . . . Checks if selected ship does not collide with missed shot. If so nothing will happen, else when it
        //           does not collide with anything, increases cells covered by the ship by 1. If ship collides with
        //           a hit, cell values will be increased by 3.

        probability_board = new int[10][10];

        // [1]
        for (GameShip ship : ships) {
            for (char orientation : new char[]{'h', 'v'}) {
                int width = 1, height = 1;
                if (orientation == 'h') {
                    width = ship.getSize();
                } else {
                    height = ship.getSize();
                }

                // [2]
                for (int y = 0; y < 11 - height; y++) {
                    for (int x = 0; x < 11 - width; x++) {

                        // [3]
                        int fits = testArea(player_board, x, y, x + width, y + height);
                        if (fits == 0) {
                            placeShipChances(x, y, x + width, y + height, 1);
                        } else if (fits == 3) {
                            placeShipChances(x, y, x + width, y + height, 3);
                        }

                    }
                }
            }
        }
    }

    /**
     * Tests what values are in given area between x-y and x1-y1(included).
     *
     * @param player_board the players board with players ships
     * @param x            Top-left corner x-axis position.
     * @param y            Top-left corner y-axis position.
     * @param x1           Bottom-right corner x-axis position.
     * @param y1           Bottom-right corner y-axis position.
     * @return 0 - if the only element in area is 0<br>2 - if there is a miss in the region<br>3 - if there is a hit
     */
    private int testArea(int[][] player_board, int x, int y, int x1, int y1) {
        int result = 0;

        for (int i = y; i < y1; i++) {
            for (int j = x; j < x1; j++) {

                if (player_board[i][j] == 2) {
                    return 2;
                } else if (player_board[i][j] == 3) {
                    result = 3;
                }
            }
        }
        return result;
    }

    /**
     * Adds the `value` to all cells between point x-y and x1-y1(including ends),
     *
     * @param x     Top-left corner x-axis position.
     * @param y     Top-left corner y-axis position.
     * @param x1    Bottom-right corner x-axis position.
     * @param y1    Bottom-right corner y-axis position.
     * @param value The added value
     */
    private void placeShipChances(int x, int y, int x1, int y1, int value) {
        for (int i = y; i < y1; i++) {
            for (int j = x; j < x1; j++) {
                if (!ignored.contains(new Pair(j, i))) {
                    probability_board[i][j] += value;
                }
            }
        }
    }

    /**
     * Looks for all shots on board, then looks around them and checks if there is an empty space, if so, it (can) mean
     * that there is the continuation of a ship.
     *
     * @param player_board the players current board with fog-of-war.
     * @return found position if there is any, else null
     */
    private int[] huntFoundShots(int[][] player_board) {

        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                if (player_board[i][j] == 3) {
                    int[] position = targetAroundFoundShots(player_board, j, i);
                    if (position != null) {
                        return position;
                    }
                }
            }
        }
        return null;
    }

    /**
     * @param player_board the players current board with fog-of-war.
     * @param x            x-axis position with a shot in it
     * @param y            y-axis position with a shot in it
     * @return position of empty cell around x-y position or null
     */
    private int[] targetAroundFoundShots(int[][] player_board, int x, int y) {
        char orientation = 'n';
        int start = 0, end = 0;

        // find orientation
        int[] x_dirs = new int[]{-1, 1, 0, 0};
        int[] y_dirs = new int[]{0, 0, -1, 1};

        for (int i = 0; i < 4; i++) {
            int[] new_pos = new int[]{x + x_dirs[i], y + y_dirs[i]};
            if (new_pos[0] >= 0 && new_pos[0] <= 9 && new_pos[1] >= 0 && new_pos[1] <= 9 && player_board[new_pos[1]][new_pos[0]]==3) {
                orientation = (x_dirs[i] == 0) ? 'v' : 'h';
            }
        }
        if (orientation=='n') return null;

        // find where is start and end
        for (int i = 0; i < 10; i++) {
            if (orientation == 'h' && x - i >= 0 && player_board[y][x - i] == 3) {
                start = x - i;
            } else if (orientation == 'h' && x + i <= 9 && player_board[y][x + i] == 3) {
                end = x + i;
            } else if (orientation == 'v' && y - i >= 0 && player_board[y - i][x] == 3) {
                start = y - i;
            } else if (orientation == 'v' && y + i <= 9 && player_board[y + i][x] == 3) {
                end = y + i;
            }
        }

        // check if it is possible
        if (orientation=='h'){
            if (start-1>=0 && player_board[y][start-1]==0) return new int[]{start-1, y};
            if (end+1<=9 && player_board[y][end+1]==0) return new int[]{end+1, y};
        } else {
            if (start-1>=0 && player_board[start-1][x]==0) return new int[]{x, start-1};
            if (end+1<=9 && player_board[end+1][x]==0) return new int[]{x, end+1};
        }

        return null;
    }


    @Override
    public String toString() {
        // Used for testing only.
        StringBuilder board = new StringBuilder();
        for (int[] line : probability_board) {
            for (int value : line) {
                board.append(value).append(" ");
            }
            board.append("\n");
        }
        return board.toString();
    }
}
