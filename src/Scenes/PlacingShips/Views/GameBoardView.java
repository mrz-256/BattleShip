package Scenes.PlacingShips.Views;

import TUI.GridTUI;

import java.util.HashMap;


/**
 * <h1>Game board View</h1>
 * Is a specific View class with pre-defined values, used in PlacingShips scene to show where has player already
 * placed ships.
 */
public class GameBoardView extends GridTUI {

    public GameBoardView(int[] position, HashMap<Integer, String> color_map) {
        super(position, new int[10][10], 2, color_map);
    }
}
