package TUI;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 * <h1>Rectangle</h1>
 * A colored text rectangle making the basic element of custom Text-User-Interface. <br>
 * Includes methods for making, loading and editing rectangles. <br>
 * There is no multiline text because it is simpler to solve elsewhere.<br>
 * Exceptions are replaced by early-return.
 */
public class RectangleTUI {
    private String[][] rectangle_shape;
    private final int[] position;

    /**
     * The amount of spaces per one 'pixel'. As this increases, the width of a pixel increases. Height is always 1.
     */
    public static int PIXEL_SIZE = 3;
    public static int maxWIDTH;
    public static int maxHEIGHT;


    // region constructor

    /**
     * This is a generic constructor for RectangleTUI object.
     *
     * @param width    Rectangle width.
     * @param height   Rectangle height.
     * @param position Top-left corner of the rectangle.
     * @param color    ANSI color code.
     */
    public RectangleTUI(int width, int height, int[] position, String color) {
        this.rectangle_shape = new String[height][width];
        fill(color);
        this.position = position;
    }

    /**
     * This constructor loads an image from a text-file given by provided path. The text file should be a CSV where each
     * value is a key to the `color_map` HashMap. Each corresponding value in `color_map` should be a string ANSI color
     * code(s).<br><br>
     * This allows loading images in an easy-to-edit file rather than in code.
     *
     * @param path      Path to the file.
     * @param color_map Color map applied to the text-file.
     * @param position  Top-left corner of the rectangle.
     */
    public RectangleTUI(String path, HashMap<String, String> color_map, int[] position) {
        // TODO: this does not load in terminal and it may be better to replace with drawMap method.
        this.position = position;
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(path));
            ArrayList<String[]> text = new ArrayList<>();
            String line;

            while ((line = bufferedReader.readLine()) != null) {
                String[] split_line = line.split(",");

                for (int i = 0; i < split_line.length; i++) {
                    split_line[i] = color_map.get(split_line[i]) + " ".repeat(PIXEL_SIZE) + ColorsTUI.END.getCode();
                }
                text.add(split_line);
            }
            bufferedReader.close();

            rectangle_shape = new String[text.size()][text.get(0).length];
            for (int i = 0; i < rectangle_shape.length; i++) {
                rectangle_shape[i] = text.get(i);
            }


        } catch (Exception e) {
            this.rectangle_shape = new String[maxHEIGHT][maxWIDTH];
            fill(ColorsTUI.GAME_BOARD.getCode());
        }

    }
    //endregion


    //region colors and text

    /**
     * This method fills whole surface with given color.
     *
     * @param color ANSI color code in string format.
     */
    public void fill(String color) {
        for (int i = 0; i < getHeight(); i++) {
            Arrays.fill(rectangle_shape[i], color + " ".repeat(PIXEL_SIZE) + ColorsTUI.END.getCode());
        }
    }

    /**
     * Fills region on rectangle.
     *
     * @param x      Top-left corner x-axis position.
     * @param y      Top-left corner y-axis position.
     * @param width  Width of the filled area.
     * @param height Height of the filled area.
     * @param color  ANSI color code.
     */
    public void fillRegion(int x, int y, int width, int height, String color) {
        if (x < 0 || y < 0 || x + width > getWidth() || y + height > getHeight()) {
            return;
        }

        for (int i = y; i < y + height; i++) {
            for (int j = x; j < x + width; j++) {
                rectangle_shape[i][j] = color + " ".repeat(PIXEL_SIZE) + ColorsTUI.END.getCode();
            }
        }
    }

    /**
     * This method 'blits' or 'prints' another rectangle on this one.
     *
     * @param other_rectangle The printed rectangle.
     */
    public void blitRectangle(RectangleTUI other_rectangle) {
        int[] size = new int[]{other_rectangle.getWidth(), other_rectangle.getHeight()};
        int[] start_pos = other_rectangle.getPosition();
        int[] end_pos = new int[]{start_pos[0] + size[0], start_pos[1] + size[1]};

        if (start_pos[0] < 0 || start_pos[1] < 0 || end_pos[0] > getWidth() || end_pos[1] > getHeight()) {
            return;
        }

        for (int i = start_pos[1]; i < end_pos[1]; i++) {
            for (int j = start_pos[0]; j < end_pos[0]; j++) {
                rectangle_shape[i][j] = other_rectangle.getAt(j - start_pos[0], i - start_pos[1]);
            }
        }
    }

    /**
     * This method similarly to the constructor with path allows 'bliting' of a 2D map onto the rectangle. The map is
     * made out of 'keys' which are used in `colors` HashMap.
     *
     * @param x      Top-left corner x-axis position.
     * @param y      Top-left corner y-axis position.
     * @param map    Integer 2D map of the 'blited' 'image'.
     * @param colors HashMap used for recognizing colors.
     * @param scale  Pixels per value in `map`.
     */
    public void drawMap(int x, int y, int[][] map, HashMap<Integer, String> colors, int scale) {
        if (x < 0 || x > getWidth() || y < 0 || y > getHeight()) {
            return;
        }

        int maximal_y = Math.min(y + map.length * PIXEL_SIZE, getHeight());
        int maximal_x = Math.min(x + map[0].length * PIXEL_SIZE, getWidth());

        for (int i = y; i < maximal_y; i++) {
            for (int j = x; j < maximal_x; j++) {
                rectangle_shape[i][j] =
                        colors.get(map[(i - y) / scale][(j - x) / scale]) + " ".repeat(PIXEL_SIZE) + ColorsTUI.END.getCode();
            }
        }
    }

    /**
     * This method allows adding one-line text to the shape-surface cells.
     *
     * @param x     Starting x-axis position.
     * @param y     Final y-axis position(this is a one-line text).
     * @param text  Text blited to the rectangle.
     * @param color ANSI color code(s) used for text.
     */
    public void addLineText(int x, int y, String text, String color) {
        if (0 > x || x > getWidth() - 1 || y < 0 || y > getHeight() - 1) {
            return;
        }

        text += " ".repeat(PIXEL_SIZE - text.length() % PIXEL_SIZE);
        int maximal_position = Math.min(getWidth(), x + text.length() / PIXEL_SIZE);

        for (int i = x; i < maximal_position; i++) {
            rectangle_shape[y][i] = color + text.substring((i - x) * PIXEL_SIZE, (i - x) * PIXEL_SIZE + PIXEL_SIZE) + ColorsTUI.END.getCode();
        }
    }
    //endregion


    // region get values
    public int getWidth() {
        return rectangle_shape[0].length;
    }

    public int getHeight() {
        return rectangle_shape.length;
    }

    public int[] getPosition() {
        return position;
    }

    public String getAt(int x, int y) {
        return rectangle_shape[y][x];
    }
    // endregion


    @Override
    public String toString() {
        StringBuilder stringSurface = new StringBuilder();
        for (String[] line : rectangle_shape) {
            for (String pixel : line) {
                stringSurface.append(pixel);
            }
            stringSurface.append('\n');
        }
        return stringSurface.toString();
    }
}
