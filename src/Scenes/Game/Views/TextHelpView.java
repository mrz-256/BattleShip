package Scenes.Game.Views;

import TUI.ColorsTUI;
import TUI.ViewTUI;

/**
 * <h1>Text Help View</h1>
 * Is a specific View class with pre-defined values used in GameScene scene as a visual help.<br><br>
 * Also may look like a 1:0.9999 copy from Scenes.PlacingShips.Views.TextHelpView, but making an generic TextView just
 * for two classes would be basically equivalent and this is easier to categorize.
 */
public class TextHelpView extends ViewTUI {

    private static final ColorsTUI COLOR = ColorsTUI.GAME_BACKGROUND;

    private static final String TEXTS =
            """
                    Commands:
                      'shoot digit latter'
                    
                      'quit'
                    
                      'restart'
                    """;

    public TextHelpView(int[] position) {
        super(new int[]{15, 6}, position, COLOR);
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
