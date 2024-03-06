package Scenes.Game;

import Logic.GameBoard;
import Logic.GameEnemy;
import Logic.GamePlayer;
import Logic.GameShip;
import Scenes.Game.Views.EnemyBoardView;
import Scenes.Game.Views.PlayerBoardView;
import Scenes.Game.Views.TextHelpView;
import TUI.ColorsTUI;
import TUI.RectangleTUI;

import java.util.HashMap;
import java.util.Scanner;
import java.util.Set;


/**
 * <h1>Game Scene</h1>
 * The important class with the actual game.
 */
public class GameScene {
    private final RectangleTUI background;

    private final GamePlayer gamePlayer;
    private final GameEnemy gameEnemy;

    private final PlayerBoardView playerBoardView;
    private final EnemyBoardView enemyBoardView;

    private final Set<GameShip> enemy_ships;

    private String warning_message;
    private boolean players_turn = true;


    public GameScene(
            HashMap<Integer, String> color_map,
            GameBoard player_board, GameBoard enemy_board,
            Set<GameShip> player_ships, Set<GameShip> enemy_ships
    ) {
        this.background = new RectangleTUI(
                43, 24,
                new int[2],
                ColorsTUI.GAME_BACKGROUND.getCode()
        );
        this.gamePlayer = new GamePlayer(enemy_board);
        this.gameEnemy = new GameEnemy(player_board, player_ships);

        this.enemy_ships = enemy_ships;

        this.playerBoardView = new PlayerBoardView(new int[]{1, 2}, color_map);
        this.enemyBoardView = new EnemyBoardView(new int[]{21, 2}, color_map);

        TextHelpView textHelpView = new TextHelpView(new int[]{2, 14});
        background.blitRectangle(textHelpView.getRectangle());

        background.addLineText(1, 1, " Your ships", ColorsTUI.GAME_DARK_GRAY.getCode());
        background.addLineText(21, 1, " Enemy ships", ColorsTUI.GAME_DARK_GRAY.getCode());
    }


    /**
     * The important method where game happens.
     *
     * @return if player wants to play again or not
     */
    public boolean playScene() {
        Scanner scanner = new Scanner(System.in);
        String line;
        int close_guard = 0;

        do {
            String winner = (enemy_ships.isEmpty()) ? "YOU!" : (gameEnemy.isPlayerDead()) ? "ENEMY!" : null;
            if (winner != null) {
                printWinner(winner);
                System.out.print(ColorsTUI.BOLD.getCode() + "├──<Play again? (yes/no)>──┤ " + ColorsTUI.END.getCode());
                return scanner.nextLine().equalsIgnoreCase("yes");
            }

            updateBackground();
            System.out.print(background);

            if (players_turn) {
                System.out.print(ColorsTUI.BOLD.getCode() + "├──<Input>──┤ " + ColorsTUI.END.getCode());
                line = scanner.nextLine().toUpperCase();
                System.out.println();

                close_guard = evaluateInput(line.split(" "));
            } else {
                players_turn = !gameEnemy.shootOnce();
            }


        } while (close_guard == 0);

        return close_guard == 2;
    }

    /**
     * Updates the visible background.
     */
    private void updateBackground() {
        background.fillRegion(0, 23, 20, 1, ColorsTUI.GAME_BACKGROUND.getCode());
        if (warning_message != null) {
            writeWarning();
            warning_message = null;
        }

        playerBoardView.setBoard(gameEnemy.getPlayerBoardWithShots().getBoard());
        playerBoardView.updateView();
        background.blitRectangle(playerBoardView.getRectangle());

        enemyBoardView.setBoard(gamePlayer.getEnemy_board_player_see().getBoard());
        enemyBoardView.updateView();
        background.blitRectangle(enemyBoardView.getRectangle());
    }

    /**
     * @param input the split, uppercase-d input player wrote
     * @return 0 - if nothing happened<br>1 - if player wants to quit<br>2 - if player wants to restart the game.
     */
    private int evaluateInput(String[] input) {
        if (input.length == 0) {
            return 0;
        }
        switch (input[0]) {
            case "QUIT" -> {
                return 1;
            }
            case "RESTART" -> {
                return 2;
            }
            case "SHOOT" -> {
                if (input.length < 3) {
                    warning_message = "Invalid/Missing location argument.";
                    return 0;
                }
                try {
                    int x = Integer.parseInt(input[1]) - 1;
                    int y = input[2].charAt(0) - 'A';
                    if (x < 0 || y < 0 || x > 9 || y > 9) {
                        warning_message = "Position out of board.";
                        break;
                    }
                    playerShoot(x, y);

                } catch (NumberFormatException e) {
                    warning_message = "Invalid location argument.";
                } catch (Exception e) {
                    warning_message = e.getMessage();
                }

            }
            default -> {
                warning_message = "Unknown command";
            }
        }
        return 0;
    }

    /**
     * Lets player see what he did wrong using a yellow text-box warning.
     */
    private void writeWarning() {
        background.addLineText(0, 23,
                warning_message,
                ColorsTUI.WARNING_YELLOW.getCode() + ColorsTUI.TEXT_BLACK.getCode());
    }

    /**
     * @param x x-axis position player wants to shoot to.
     * @param y x-axis position player wants to shoot to.
     */
    private void playerShoot(int x, int y) {
        players_turn = gamePlayer.shootOnce(x, y);

        for (GameShip ship : enemy_ships) {
            boolean sunk = ship.tryShoot(x, y);
            if (sunk) {
                gamePlayer.clearAroundSunkShip(ship.getStart_and_end());
                enemy_ships.remove(ship);
            }
        }
    }

    /**
     * Prints to console who has won.
     *
     * @param winner the winners name
     */
    private void printWinner(String winner) {
        RectangleTUI rectangle = new RectangleTUI(43, 3, new int[2], ColorsTUI.WINNER.getCode());
        rectangle.addLineText(1, 1, "The winner is: " + winner, ColorsTUI.WINNER.getCode() + ColorsTUI.TEXT_BLACK.getCode());
        System.out.println(rectangle);
    }

}
