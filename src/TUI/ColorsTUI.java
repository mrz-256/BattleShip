package TUI;

/**
 * <h1>Color Enum</h1>
 * Stores ANSI color codes in an accessible way.
 */
public enum ColorsTUI {

    END("\033[0m"),

    BLACK("\033[48;5;0m"),
    PLAYER_BLUE("\033[48;5;21m"),
    ENEMY_RED("\033[48;5;124m"),
    WARNING_YELLOW("\033[48;5;3m"),
    WINNER("\033[48;5;227m"),

    MENU_GRAY_LIGHT("\033[48;5;240m"),
    MENU_GRAY_DARK("\033[48;5;236m"),
    MENU_WAVE("\033[48;5;12m"),

    GAME_BACKGROUND("\033[48;5;59m"),
    GAME_BOARD("\033[48;5;24m"),
    GAME_SUNK("\033[48;5;238m"),
    GAME_DARK_GRAY("\033[48;5;242m"),
    GAME_SHIP("\033[48;5;235m"),
    GAME_MISSED("\033[48;5;110m"),


    TEXT_BLACK("\033[30m"),
    BOLD("\033[1m");

    private final String color_code;

    public String getCode(){
        return this.color_code;
    }

    ColorsTUI(String color_code){
        this.color_code = color_code;
    }
}
