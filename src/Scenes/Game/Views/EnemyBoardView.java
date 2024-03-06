package Scenes.Game.Views;

import TUI.GridTUI;

import java.util.HashMap;


/**
 * <h1>Enemy Board View</h1>
 * The enemy board view.
 */
public class EnemyBoardView extends GridTUI {

    public EnemyBoardView(int[] position, HashMap<Integer, String> color_map) {
        super(position, new int[10][10], 2, color_map);
    }
}
