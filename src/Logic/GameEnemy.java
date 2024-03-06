package Logic;

import java.util.Random;
import java.util.Set;


/**
 * <h1>Game Enemy</h1>
 * A class representing the enemy player.
 */
public class GameEnemy {
    private final Set<GameShip> players_ships;

    private final GameBoard player_board;
    private final GameBoard player_board_enemy_see;
    private final GameBoard player_board_with_shots;

    private final GameEnemyLogic enemyLogic;

    /**
     * The version of players-board except that it is a combination of whole_board and board the board_enemy_sees.<br>
     * It shows both ships, sunk ships and misses and so it is displayed to player.
     * @return the board displayed to player
     */
    public GameBoard getPlayerBoardWithShots() {
        return player_board_with_shots;
    }

    public GameEnemy(GameBoard player_board, Set<GameShip> players_ships) {
        this.player_board = player_board;
        this.players_ships = players_ships;
        this.player_board_enemy_see = new GameBoard();
        this.player_board_with_shots = player_board;
        this.enemyLogic = new GameEnemyLogic();
    }

    /**
     * @return if enemy won or not.
     */
    public boolean isPlayerDead() {
        return players_ships.isEmpty();
    }

    /**
     * Used to get the position where enemy wants to shoot.
     *
     * @return the position where the enemy shoots.
     */
    public boolean shootOnce() {
        int[] position = enemyLogic.getNext(player_board_enemy_see.getBoard(), players_ships);

        player_board_with_shots.shoot(position[0], position[1], player_board);
        boolean hit = player_board_enemy_see.shoot(position[0], position[1], player_board);

        for (GameShip ship : players_ships) {

            boolean sunk = ship.tryShoot(position[0], position[1]);
            if (sunk) {
                int[] body_pos = ship.getStart_and_end();
                player_board_with_shots.clearAroundSunkShip(body_pos[0], body_pos[1], body_pos[2], body_pos[3]);
                player_board_enemy_see.clearAroundSunkShip(body_pos[0], body_pos[1], body_pos[2], body_pos[3]);

                players_ships.remove(ship);
                break;
            }
        }
        return hit;
    }

}
