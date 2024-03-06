package Scenes.Game.Views;

import TUI.GridTUI;

import java.util.HashMap;


/**
 * <h1>Player board View</h1>
 * The player board view.
 */
public class PlayerBoardView extends GridTUI {

    public PlayerBoardView(int[] position, HashMap<Integer, String> color_map) {
        super(position, new int[10][10], 1, color_map);
    }
}
