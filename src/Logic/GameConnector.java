package Logic;

import Scenes.Game.GameScene;
import Scenes.Menu.MenuScene;
import Scenes.PlacingShips.PlacingShipsScene;
import TUI.ColorsTUI;

import java.util.HashMap;
import java.util.Set;

/**
 * <h1>Game Connector</h1>
 * The class which holds everything in the program together, runs scenes
 */
public class GameConnector {

    /**
     * The array representing ships in game, each value represents the ship size except the last one which exists to
     * avoids some minor problems.
     */
    public static final int[] SHIP_SIZES = new int[]{5, 4, 3, 2, 2, 1, 1, 0};
    private static final HashMap<Integer, String> color_map = new HashMap<>();

    public GameConnector() {
        color_map.put(0, ColorsTUI.GAME_BOARD.getCode());
        color_map.put(1, ColorsTUI.GAME_SHIP.getCode());
        color_map.put(2, ColorsTUI.GAME_MISSED.getCode());
        color_map.put(3, ColorsTUI.GAME_SUNK.getCode());
        color_map.put(4, ColorsTUI.GAME_DARK_GRAY.getCode());
        color_map.put(5, ColorsTUI.GAME_BACKGROUND.getCode());
    }

    /**
     * This plays the WHOLE game including all scenes.
     *
     * @return false - if game could not be loaded or player does not want to play again<br>true - if player wants to play again
     */
    public boolean play() {
        // MENU SCENE
        if (!new MenuScene().playScene()) {
            return false;
        }

        // PLACE SHIPS SCENE
        PlacingShipsScene placingShipsScene = new PlacingShipsScene(color_map, SHIP_SIZES);
        GameBoard player_board = placingShipsScene.playScene();
        Set<GameShip> player_ships = placingShipsScene.getPlacedShips();
        if (player_board == null) {
            return false;
        }

        GameBoard enemy_board = new GameBoard();
        Set<GameShip> enemy_ships = enemy_board.placeShipsRandomly(SHIP_SIZES, 0);


        // GAME SCENE
        GameScene gameScene = new GameScene(color_map, player_board, enemy_board, player_ships, enemy_ships);
        return gameScene.playScene();
    }

}
