import Logic.GameConnector;
import TUI.RectangleTUI;

public class Main {

    public static final int WIDTH = 43;
    public static final int HEIGHT = 24;
    public static int PIXEL_SIZE = 3;


    public static void main(String[] args) {

        // SET UP
        if (args.length >= 1) {
            PIXEL_SIZE = Math.max(2, args[0].charAt(0) - '0');
            RectangleTUI.PIXEL_SIZE = PIXEL_SIZE;
        }
        RectangleTUI.maxWIDTH = WIDTH;
        RectangleTUI.maxHEIGHT = HEIGHT;

        System.out.println("\033[8;" + (HEIGHT + 1) + ";" + (WIDTH * PIXEL_SIZE) + "t");


        // GAME
        GameConnector game;
        do {
            game = new GameConnector();
        } while (game.play());



        /*
         * Comment:
         * - ANSI: https://gist.github.com/fnky/458719343aabd01cfb17a3a4f7296797
         *
         * To add:
         * - updateProbability() method is super slow
         *
         * Log since 1.0:
         * - player can no longer shoot at the same spot twice (added in GameBoard shoot method)
         * - warning when invalid command instead of ignoring it
         * - better terminal pixels using args
         *
         * - When I let two instances of GameEnemyLogic play against each other, the mean total amount of steps per game
         *   used to be 91.14454. Now it is downgraded to 97.7941, I think(?) it makes the bot look smarter against a
         *   player, but it probably to-be-deleted.
         *
         */

        // region PARALLEL TESTING
/*
        int threads = 1_000;
        Testing[] testings = new Testing[threads];
        for (int i = 0; i < testings.length; i++) {
            testings[i] = new Testing();
            testings[i].start();
        }

        boolean run = true;
        while (run) {
            run = false;
            for (Testing t : testings) {
                if (t.doesLive()) {
                    run = true;
                }
            }
        }

        int steps = 0;
        for (Testing t : testings) {
            steps += t.getSteps();
        }
        System.out.println(steps + " / " + threads + " = " + (double)steps/(double)threads);*/
        // endregion

        // region SERIAL TESTING
        /*
        Testing t = new Testing();
        int stp = 0;
        for (int i = 0; i < 10_000; i++) {
            stp += t.compareOnce();
        }
        System.out.println((double)stp/(double)10_000);
        */
        // endregion

    }
}