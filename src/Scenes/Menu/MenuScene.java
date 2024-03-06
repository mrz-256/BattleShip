package Scenes.Menu;

import TUI.ColorsTUI;
import TUI.RectangleTUI;

import java.util.HashMap;
import java.util.Scanner;

/**
 * <h1>Menu Scene</h1>
 * A scene used to ask player whatever they want to play or not.<br>
 * The necessity of this class is questionable.
 */
public class MenuScene {
    private static final HashMap<String, String> color_map = new HashMap<>();

    private final RectangleTUI background;

    public MenuScene() {
        color_map.put("0", ColorsTUI.GAME_BOARD.getCode());
        color_map.put("1", ColorsTUI.MENU_GRAY_LIGHT.getCode());
        color_map.put("2", ColorsTUI.MENU_GRAY_DARK.getCode());
        color_map.put("3", ColorsTUI.ENEMY_RED.getCode());
        color_map.put("4", ColorsTUI.PLAYER_BLUE.getCode());
        color_map.put("5", ColorsTUI.BLACK.getCode());
        color_map.put("6", ColorsTUI.MENU_WAVE.getCode());

        background = new RectangleTUI(
                "src/Scenes/Menu/menu_background.txt",
                color_map,
                new int[2]
        );

        writeText();
    }

    /**A method to show the scene and ask player if they want to play. <br>
     * Will loop endlessly until user types either "play" or "quit".
     * @return true if user wants to play, false otherwise
     */
    public boolean playScene() {
        Scanner scanner = new Scanner(System.in);
        String input;

        do {
            System.out.print(background);
            System.out.print(ColorsTUI.BOLD.getCode() + "├──<Input>──┤ " + ColorsTUI.END.getCode());
            input = scanner.nextLine();

            if (input.equalsIgnoreCase("play")) {
                return true;
            } else if (input.equalsIgnoreCase("quit")) {
                return false;
            }

        } while (true);
    }

    /**
     * Writes two short guide messages on background.
     */
    private void writeText(){
        String play_text = "Write `Play` to play.";
        background.addLineText(
                (background.getWidth() - play_text.length()/3)/2, 11,
                play_text,
                ColorsTUI.GAME_BOARD.getCode() + ColorsTUI.BOLD.getCode() + ColorsTUI.TEXT_BLACK.getCode()
        );

        String quit_text = "or `Quit` to quit.";
        background.addLineText(
                (background.getWidth() - quit_text.length()/3)/2, 12,
                quit_text,
                ColorsTUI.GAME_BOARD.getCode() + ColorsTUI.BOLD.getCode() + ColorsTUI.TEXT_BLACK.getCode()
        );
    }


}
