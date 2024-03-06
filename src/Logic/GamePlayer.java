package Logic;

/**
 * <h1>Game Player class</h1>
 * A class representing players values such as the board he sees as well as the action he can do (shoot).
 */
public class GamePlayer {

    private final GameBoard enemy_board;
    private final GameBoard enemy_board_player_see;


    public GameBoard getEnemy_board_player_see() {
        return enemy_board_player_see;
    }

    public GamePlayer(GameBoard enemy_board) {
        this.enemy_board = enemy_board;
        this.enemy_board_player_see = new GameBoard();
    }

    /**
     * Lets player shoot once.
     *
     * @param x x-axis location where player shoots.
     * @param y y-axis location where player shoots.
     * @return if hit or not.
     */
    public boolean shootOnce(int x, int y){
        return enemy_board_player_see.shoot(x, y, enemy_board);
    }

    /**
     * Clears the area on 'enemies board the player see' around sunken ship with missed shots.
     *
     * @param body_pos the body parts positions, in form of [x, y, x1, y1]. X and Y are head position, X1 and Y1 are tail.
     */
    public void clearAroundSunkShip(int[] body_pos) {
        enemy_board_player_see.clearAroundSunkShip(body_pos[0], body_pos[1], body_pos[2], body_pos[3]);
    }


}
