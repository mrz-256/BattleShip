package Scenes.PlacingShips.Views;

import TUI.ColorsTUI;
import TUI.ViewTUI;

/**
 * <h1>Current ship View</h1>
 * <p>Is a specific View class with pre-defined values used in PlacingShips scene as a visual help.</p>
 * Shows information (size and orientation) about the ship which is the player supposed to place next
 */
public class CurrentShipView extends ViewTUI {

    public static final ColorsTUI BOARD_COLOR = ColorsTUI.MENU_GRAY_LIGHT;
    public static final ColorsTUI TITLE_COLOR = ColorsTUI.GAME_DARK_GRAY;
    public static final ColorsTUI SHIP_COLOR = ColorsTUI.GAME_SHIP;
    public static final ColorsTUI BACKGROUND_COLOR = ColorsTUI.GAME_BACKGROUND;

    public static final String TITLE_TEXT = " Current ship";

    private final int[] ships;
    private int current_ship_index;
    private char orientation;


    /**
     * Sets up values of the View. Used in outside class to update adequately.
     *
     * @param current_ship_index The index representing the current ship in the `ships` array.
     * @param orientation        The orientation of the ship. Either 'h' or 'v' for horizontal or vertical.
     */
    public void setShipValues(int current_ship_index, char orientation) {
        this.current_ship_index = current_ship_index;
        this.orientation = orientation;
    }

    public CurrentShipView(int[] position, int[] ships) {
        super(new int[]{7, 8}, position, BACKGROUND_COLOR);
        this.ships = ships;
        this.current_ship_index = 0;
        this.orientation = 'h';
    }

    /**
     * An inherited method used to update the View.
     */
    public void updateView() {

        rectangle.fill(BOARD_COLOR.getCode());
        rectangle.fillRegion(0, 0, rectangle.getWidth(), 1, BACKGROUND_COLOR.getCode());
        rectangle.addLineText(0, 0, TITLE_TEXT, TITLE_COLOR.getCode());

        int x_padd = (rectangle.getWidth() - ships[current_ship_index]) / 2;
        int y_padd = (rectangle.getWidth()) / 2;

        int width = ships[current_ship_index];
        int height = 1;

        if (orientation == 'v') {
            int temp = x_padd;
            x_padd = y_padd;
            y_padd = temp;

            temp = width;
            width = height;
            height = temp;
        }

        rectangle.fillRegion(
                x_padd, y_padd + 1,
                width, height,
                SHIP_COLOR.getCode()
        );
    }
}
