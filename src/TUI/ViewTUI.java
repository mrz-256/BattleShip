package TUI;

/**<h1>View</h1>
 * An abstract wrapper class around `RectangleTUI`, makes it more usable for specific cases.
 */
public abstract class ViewTUI {
    protected final RectangleTUI rectangle;

    public RectangleTUI getRectangle() {
        return rectangle;
    }

    public ViewTUI(int[] size, int[] position, ColorsTUI color) {
        this.rectangle = new RectangleTUI(
                size[0], size[1],
                position,
                color.getCode()
        );
    }

    /** Abstract method which updates the view. */
    public abstract void updateView();

}
