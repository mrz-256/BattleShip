package Scenes.PlacingShips.Views;

import TUI.ColorsTUI;
import TUI.RectangleTUI;
import TUI.ViewTUI;

/**
 * <h1>Text Help View</h1>
 * Is a specific View class with pre-defined values used in PlacingShips scene as a visual help. Shows possible commands
 * player can use.
 */
public class TextHelpView extends ViewTUI {

    private static final ColorsTUI COLOR = ColorsTUI.GAME_BACKGROUND;

    private static final String TEXTS =
            """     
                    Commands:
                      'rotate'
                                
                      'place digit latter'
                                
                      'random'
                                
                      'reset'
                                
                      'confirm'
                                
                      'quit'
                    """;


    public TextHelpView(int[] position) {
        super(new int[]{11, 13}, position, COLOR);
        updateView();
    }

    /**
     * Renders the text on the View. <br>
     * As text does not change, there is no reason to ever call this method.
     */
    @Override
    public void updateView() {
        String[] split_text = TEXTS.split("\n");
        for (int i = 0; i < split_text.length; i++) {
            rectangle.addLineText(0, i, split_text[i], COLOR.getCode());
        }
    }
}
