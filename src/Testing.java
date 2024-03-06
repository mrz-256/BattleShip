import Logic.GameBoard;
import Logic.GameEnemyLogic;
import Logic.GameShip;


import java.util.Set;


/**
 * A class used for counting the amount of steps one game takes. Also for speed.
 */
public class Testing implements Runnable {
    private static final int[] ships = new int[]{5, 4, 3, 2, 2, 1, 1};
    private int steps = 0;
    private Thread thread;

    public int compareOnce() {
        GameEnemyLogic ALogic = new GameEnemyLogic();   // The logic of bot A
        GameBoard ABoard = new GameBoard();             // The board of bot A
        GameBoard ABoardEmpty = new GameBoard();        // The board of bot A
        Set<GameShip> AShips = ABoard.placeShipsRandomly(ships, 0); // The ships of bot A

        GameEnemyLogic BLogic = new GameEnemyLogic();
        GameBoard BBoard = new GameBoard();
        GameBoard BBoardEmpty = new GameBoard();
        Set<GameShip> BShips = BBoard.placeShipsRandomly(ships, 0);


        int step_count = 0;

        boolean APlays = true;
        while (!AShips.isEmpty() && !BShips.isEmpty()) {

            if (APlays) {
                int[] shoot_location = ALogic.getNext(BBoardEmpty.getBoard(), BShips);
                APlays = BBoardEmpty.shoot(shoot_location[0], shoot_location[1], BBoard);

                if (APlays) {
                    for (GameShip ship : BShips) {
                        boolean dead = ship.tryShoot(shoot_location[0], shoot_location[1]);
                        if (dead) {
                            int[] body = ship.getStart_and_end();
                            BBoardEmpty.clearAroundSunkShip(body[0], body[1], body[2], body[3]);
                            BShips.remove(ship);
                            break;
                        }
                    }
                }

            } else {
                int[] shoot_location = BLogic.getNext(ABoardEmpty.getBoard(), AShips);
                APlays = !ABoardEmpty.shoot(shoot_location[0], shoot_location[1], ABoard);

                if (!APlays) {
                    for (GameShip ship : AShips) {
                        boolean dead = ship.tryShoot(shoot_location[0], shoot_location[1]);
                        if (dead) {
                            int[] body = ship.getStart_and_end();
                            ABoardEmpty.clearAroundSunkShip(body[0], body[1], body[2], body[3]);
                            AShips.remove(ship);
                            break;
                        }
                    }
                }

            }
            step_count++;
        }
        return step_count;
    }

    // region get
    public int getSteps() {
        return steps;
    }

    public boolean doesLive() {
        return thread != null && thread.isAlive();
    }
    // endregion

    // region runnable
    @Override
    public void run() {
        steps += compareOnce();
    }

    public void start() {
        if (thread == null) {
            thread = new Thread(this, "thread");
            thread.start();
        }
    }
    //endregion

}
