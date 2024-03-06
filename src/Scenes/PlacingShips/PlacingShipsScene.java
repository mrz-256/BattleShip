package Scenes.PlacingShips;

import Logic.GameBoard;
import Logic.GameShip;
import Scenes.PlacingShips.Views.CurrentShipView;
import Scenes.PlacingShips.Views.GameBoardView;
import Scenes.PlacingShips.Views.ShipsListView;
import Scenes.PlacingShips.Views.TextHelpView;
import TUI.ColorsTUI;
import TUI.RectangleTUI;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

/**
 * <h1>Placing Ships Scene</h1>
 * This is a scene used to let player place ships.<br>
 * <br>
 * <p>As a scene, this class includes System.out and System.in</p>
 * <p>There are many -View classes used in here made specifically for
 * this class with hard-coded values in them, (except of position as if I decided to move one thing, it would be useful
 * to have other positions around as well so that I can slightly adjust them too).
 */
public class PlacingShipsScene {
    /** The background image showing everything to the user. */
    private final RectangleTUI background;

    // region -Views
    private final ShipsListView shipsListView;
    private final CurrentShipView currentShipView;
    private final GameBoardView gameBoardView;
    // endregion

    // region variables and finals
    private final int[] ship_sizes;
    private final GameBoard gameBoard;
    private final Set<GameShip> placed_ships;
    private int current_ship_index;
    private char orientation;
    private String warning_message;
    // endregion

    // region get
    public Set<GameShip> getPlacedShips() {
        return placed_ships;
    }
    // endregion

    // region constructor
    /**
     * @param color_map  the map used to color gameBoard.
     * @param ship_sizes the amount of ships and their sizes are determined by this array.
     */
    public PlacingShipsScene(HashMap<Integer, String> color_map, int[] ship_sizes) {
        this.background = new RectangleTUI(
                RectangleTUI.maxWIDTH, RectangleTUI.maxHEIGHT,
                new int[2],
                ColorsTUI.GAME_BACKGROUND.getCode()
        );
        this.ship_sizes = ship_sizes;
        this.gameBoard = new GameBoard();
        this.placed_ships = new HashSet<>();

        this.shipsListView = new ShipsListView(new int[]{1, 1}, this.ship_sizes);
        this.currentShipView = new CurrentShipView(new int[]{11, 1}, this.ship_sizes);
        this.gameBoardView = new GameBoardView(new int[]{21, 2}, color_map);
        background.blitRectangle(new TextHelpView(new int[]{10, 10}).getRectangle());

        current_ship_index = 0;
        orientation = 'h';
    }
    // endregion

    /**
     * Called to let player place ships. <br>
     * Includes System.out and System.in
     *
     * @return a GameBoard with placed ships.
     */
    public GameBoard playScene() {
        Scanner scanner = new Scanner(System.in);
        String input;
        int close_guard;

        do {
            updateBackground();

            System.out.print(background);
            System.out.print(ColorsTUI.BOLD.getCode() + "├──<Input>──┤ " + ColorsTUI.END.getCode());
            input = scanner.nextLine().toUpperCase();

            close_guard = evaluateInput(input.split(" "));

        } while (close_guard == 0);

        return (close_guard == 2) ? gameBoard : null;

    }

    /**
     * This method updates background rectangle image.
     */
    private void updateBackground() {
        background.fillRegion(0, RectangleTUI.maxHEIGHT - 1, RectangleTUI.maxWIDTH, 1, ColorsTUI.GAME_BACKGROUND.getCode());
        if (warning_message != null) {
            writeWarning();
            warning_message = null;
        }

        shipsListView.setCurrent_ship_index(current_ship_index);
        shipsListView.updateView();
        background.blitRectangle(shipsListView.getRectangle());

        currentShipView.setShipValues(current_ship_index, orientation);
        currentShipView.updateView();
        background.blitRectangle(currentShipView.getRectangle());

        gameBoardView.setBoard(gameBoard.getBoard());
        gameBoardView.updateView();
        background.blitRectangle(gameBoardView.getRectangle());
    }

    /**
     * A method to evaluate user input, writes simple one-line text warnings when error occurs.
     *
     * @param input System.in input formatted into array of strings, used to know what to do.
     * @return 0 when nothing happens <br> 1 when QUIT was selected <br> 2 when CONFIRM was selected
     */
    private int evaluateInput(String[] input) {
        if (input.length == 0) {
            return 0;
        }
        switch (input[0]) {
            case "ROTATE" -> orientation = (orientation == 'v') ? 'h' : 'v';
            case "PLACE" -> {
                if (current_ship_index == ship_sizes.length - 1) {
                    warning_message = "Already placed all ships. Write 'confirm' to continue.";
                    break;
                }

                try {
                    int x = Integer.parseInt(input[1]) - 1;
                    int y = input[2].charAt(0) - 'A';
                    placeShip(x, y);
                } catch (Exception e) {
                    warning_message = "Invalid position.";
                }
            }
            case "RANDOM" -> {
                Set<GameShip> newly_placed_ships = gameBoard.placeShipsRandomly(ship_sizes, current_ship_index);
                placed_ships.addAll(newly_placed_ships);
                current_ship_index = ship_sizes.length - 1;
            }
            case "RESET" -> {
                gameBoard.setAllToDefault();
                current_ship_index = 0;
                placed_ships.clear();
                orientation = 'h';
            }
            case "CONFIRM" -> {
                if (current_ship_index == ship_sizes.length - 1) {
                    return 2;
                }
                warning_message = "You must place all ships first.";
            }
            case "QUIT" -> {
                return 1;
            }
            default -> {
                warning_message = "Unknown command";
            }
        }
        return 0;
    }

    /**
     * Tries to place a new ship on board, if successful, will add the ship GameShip object into a set.
     *
     * @param x x-axis position of the top-left corner of the ship on `GameBoard` board.
     * @param y y-axis position of the top-left corner of the ship on `GameBoard` board.
     */
    private void placeShip(int x, int y) {
        try {
            GameShip newly_placed_ship = gameBoard.safePlaceShip(x, y, ship_sizes[current_ship_index], orientation);
            placed_ships.add(newly_placed_ship);
            current_ship_index++;
        } catch (Exception e) {
            warning_message = e.getMessage();
        }
    }

    /**
     * Used to warn user about something using a one-line text.
     */
    private void writeWarning() {
        background.addLineText(0, 23,
                warning_message,
                ColorsTUI.WARNING_YELLOW.getCode() + ColorsTUI.TEXT_BLACK.getCode());
    }

}
