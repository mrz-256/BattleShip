package TUI;

import java.util.HashMap;

/**
 * <h1>Grid</h1>
 * <p>Used to make grids with integer coordinates for X-axis and alphabetic for Y-axis.</p>
 * <p>Has already pre-defined unchangeable colors.</p>
 */
public class GridTUI extends ViewTUI {

    private int[][] board;
    private final HashMap<Integer, String> color_map;
    private final int scale;

    private static final ColorsTUI BOARD_COLOR = ColorsTUI.GAME_BOARD;
    private static final ColorsTUI GRID_COLOR = ColorsTUI.GAME_DARK_GRAY;


    /**
     * Updates board.
     *
     * @param board new board
     */
    public void setBoard(int[][] board) {
        this.board = board;
    }

    /**
     * @param position Top-left corner position.
     * @param board The (information) board with ships etc.
     * @param scale The scale of the board.
     * @param color_map The map binding values in `board` as keys here. Values in should be ANSI color codes.
     */
    public GridTUI(int[] position, int[][] board, int scale, HashMap<Integer, String> color_map) {
        super(new int[]{board[0].length * scale + 1, board.length * scale + 1}, position, BOARD_COLOR);
        this.board = board;
        this.color_map = color_map;
        this.scale = scale;

        // the background for coordinates
        rectangle.fillRegion(0, 0, 1, board.length * scale + 1, GRID_COLOR.getCode());
        rectangle.fillRegion(0, 0,  board.length * scale + 1, 1, GRID_COLOR.getCode());

        String space = (RectangleTUI.PIXEL_SIZE>2)?" ": "";

        for (int i = 1; i < board.length * scale + scale; i += scale) {
            rectangle.addLineText(i, 0, " " + ((i-1) / scale+1) + " ", GRID_COLOR.getCode());

            rectangle.addLineText(0, i, space + (char) ('A' + (i-1) / scale), GRID_COLOR.getCode());
        }
    }


    @Override
    public void updateView() {
        rectangle.drawMap(1, 1, board, color_map, scale);
    }
}
