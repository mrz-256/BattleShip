package Scenes.PlacingShips.Views;

import TUI.ColorsTUI;
import TUI.ViewTUI;

/**
 * <h1>Ships List View</h1>
 * Is a specific View class with pre-defined values used in PlacingShips scene as a visual help.
 * Shows remaining ships necessary to add and their sizes.
 */
public class ShipsListView extends ViewTUI {

    private static final ColorsTUI TITLE_COLOR = ColorsTUI.GAME_DARK_GRAY;
    private static final ColorsTUI SHIP_COLOR = ColorsTUI.GAME_SHIP;
    private static final ColorsTUI LIST_COLOR = ColorsTUI.MENU_GRAY_LIGHT;
    private static final ColorsTUI BACKGROUND_COLOR = ColorsTUI.GAME_BACKGROUND;

    private static final String TITLE_TEXT = " Your ships.";

    private final int[] ships;
    private int current_ship_index;


    /**Updates values.
     * @param current_ship_index the index representing the current ship in 'ships' array.
     */
    public void setCurrent_ship_index(int current_ship_index) {
        this.current_ship_index = current_ship_index;
    }

    public ShipsListView(int[] position, int[] ships) {
        super(new int[]{8, 19}, position, BACKGROUND_COLOR);
        this.ships = ships;
        this.current_ship_index = 0;
    }

    /** A method to update the View. */
    public void updateView() {
        rectangle.fill(LIST_COLOR.getCode());
        rectangle.fillRegion(0, 0, rectangle.getWidth(), 1, BACKGROUND_COLOR.getCode());
        rectangle.addLineText(0, 0, TITLE_TEXT, TITLE_COLOR.getCode());

        for (int i = current_ship_index + 1; i < ships.length; i++) {
            int y_item_position = (i - current_ship_index) * 2;
            rectangle.fillRegion(
                    1, y_item_position,
                    ships[i], 1,
                    SHIP_COLOR.getCode()
            );
        }
    }

}
