package Logic;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

/**
 * <h1>Game-board class</h1>
 * <p>A data class representing the game boards where boats are placed.</p>
 * <p>Has methods for adding new ships.</p>
 */
public class GameBoard {
    private int[][] board;


    // region get and set

    /**
     * It is easier to work directly with a 2D array rather than with now removed getAt() method.
     *
     * @return the 2D int array of the board
     */
    public int[][] getBoard() {
        return board;
    }

    /**
     * Resets whole board to 0. Useful when resetting/setting the game.
     */
    public void setAllToDefault() {
        board = new int[10][10];
    }
    // endregion

    public GameBoard() {
        setAllToDefault();
    }

    // region place ship
    /**
     * Method to safely place one ship of given position, size and orientation on board.<br>
     * As name suggests this method checks collisions and safety of placement.<br>
     * The placed ship is also created as a GameShip object which is then returned.
     *
     * @param x           x-axis position of the top-left corner of the ship.
     * @param y           x-axis position of the top-left corner of the ship.
     * @param size        the length of a ship
     * @param orientation The orientation of a ship, either 'h' or 'v' for horizontal or vertical
     * @return a newly placed GameShip object
     * @throws Exception In case the ship does not fit on board or would collide with other ship.
     */
    public GameShip safePlaceShip(int x, int y, int size, char orientation) throws Exception {
        if (x < 0 || y < 0 || x > 9 || y > 9 || (orientation == 'v' && y + size > 10) || (orientation == 'h' && x + size > 10)) {
            throw new Exception("Ship does not fit on board.");
        }
        int horizontal_length = size;
        int vertical_length = 1;

        if (orientation == 'v') {
            vertical_length = size;
            horizontal_length = 1;
        }
        if (!isAreaSafe(x, y, horizontal_length, vertical_length)) {
            throw new Exception("Ship collides with other ship.");
        }
        return placeShip(x, y, horizontal_length, vertical_length);


    }

    /**
     * Checks if it is safe to place a ship at given position of given size.
     *
     * @param x                 x-axis position of the top-left corner of the ship.
     * @param y                 x-axis position of the top-left corner of the ship.
     * @param horizontal_length the horizontal length (width) of the ship.
     * @param vertical_length   the vertical length (height) of the ship.
     * @return if the area ship will take + 1 on all sides is empty.
     */
    private boolean isAreaSafe(int x, int y, int horizontal_length, int vertical_length) {
        for (int i = Math.max(y - 1, 0); i < Math.min(y + vertical_length + 1, 10); i++) {
            for (int j = Math.max(x - 1, 0); j < Math.min(x + horizontal_length + 1, 10); j++) {
                if (board[i][j] != 0) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * <p>A method to place a ship whatever it collides with other ship or not.</p>
     * <p> X nor Y coordinates are safety checked as it should already be done in parent method. </p>
     *
     * @param x                 x-axis position of the top-left corner of the ship.
     * @param y                 x-axis position of the top-left corner of the ship.
     * @param horizontal_length horizontal length (width) of the ship.
     * @param vertical_length   vertical length (height) of the ship.
     * @return a newly placed GameShip object
     */
    private GameShip placeShip(int x, int y, int horizontal_length, int vertical_length) {
        for (int i = y; i < y + vertical_length; i++) {
            for (int j = x; j < x + horizontal_length; j++) {
                board[i][j] = 1;
            }
        }
        return new GameShip(x, y, x + horizontal_length, y + vertical_length);
    }

    /**
     * This method places a ship at random position and orientation on the board.<br>
     * It achieves so by selecting random position and orientation in a loop until it finds a valid one.
     *
     * @param size the size of the given ship which is being randomly added.
     * @return a newly placed GameShip object
     */
    public GameShip placeShipRandomly(int size) {
        Random random = new Random();
        int x, y, horizontal_length, vertical_length;

        do {
            horizontal_length = size;
            vertical_length = 1;

            if (random.nextInt(2) == 1) {
                vertical_length = size;
                horizontal_length = 1;
            }

            x = random.nextInt(11 - horizontal_length);
            y = random.nextInt(11 - vertical_length);

            if (isAreaSafe(x, y, horizontal_length, vertical_length)) {
                return placeShip(x, y, horizontal_length, vertical_length);
            }
        } while (true);
    }


    /**
     * Places all ships in `ship_sizes` from `current_ship_index` up to the pre-last element using the
     * `placeShipRandomly()` method on each size.
     *
     * @param ship_sizes         the ship_sizes array representing all ship_sizes.
     * @param current_ship_index the index in ship_sizes, from this index to the end of the array, ship_sizes will be added.
     * @return a set of all newly placed GameShip objects
     */
    public Set<GameShip> placeShipsRandomly(int[] ship_sizes, int current_ship_index) {
        Set<GameShip> placed_ships = new HashSet<>();
        for (int i = current_ship_index; i < ship_sizes.length - 1; i++) {
            placed_ships.add(placeShipRandomly(ship_sizes[i]));
        }
        return placed_ships;
    }
    // endregion

    /**
     * Shoots one shot at the selected position.<br>
     * <br>
     * This is intended to be used on the 'fog-of-war' empty map the opponent sees as the shot is compared to the
     * `whole_board` board which is supposed to be the whole opponents board with all ships uncovered.<br>
     * <br>
     * If the `whole_board` at the position x-y is ship(represented by value 1), the value in `this` board at position
     * x-y will be set to 3(representing a sunken ship) and value `true` will be returned.<br>
     * <br>
     * Else when shooter has shot on already discovered position with value such as 2(representing a miss) or 3,
     * `true` will be returned again as it means shooter should be allowed to shoot again.<br>
     * <br>
     * When board position is not a hit and neither is it an already found position, the location in `this` board will
     * be set to 2 and `false` will be returned.
     *
     * @param x           x-axis location of the shot.
     * @param y           y-axis position of the shot.
     * @param whole_board the whole board and not just the fog-of-war version.
     * @return false when shooter missed true otherwise.
     */
    public boolean shoot(int x, int y, GameBoard whole_board) {
        if (board[y][x] == 2) {
            return true;
        }

        if (whole_board.getBoard()[y][x] == 1 || whole_board.getBoard()[y][x] == 3) {
            board[y][x] = 3;
            return true;
        }
        board[y][x] = 2;
        return false;
    }

    /**
     * Surrounds area around a sunk ship with missed shots(with value 2) to show that no ship can possibly be there.
     *
     * @param x  x-axis position of the start of the ship.
     * @param y  y-axis position of the start of the ship.
     * @param x1 x-axis position of the end of the ship.
     * @param y1 y-axis position of the end of the ship.
     */
    public void clearAroundSunkShip(int x, int y, int x1, int y1) {
        for (int i = Math.max(0, y - 1); i < Math.min(board.length, y1 + 1); i++) {
            for (int j = Math.max(0, x - 1); j < Math.min(board.length, x1 + 1); j++) {
                if (board[i][j] == 0) {
                    board[i][j] = 2;
                }
            }
        }
    }
}
