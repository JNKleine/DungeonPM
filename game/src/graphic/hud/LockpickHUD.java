package graphic.hud;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Align;
import controller.ScreenController;
import tools.Constants;
import tools.Point;

public class LockpickHUD <T extends Actor> extends ScreenController<T> {
    /**
     * Creates a Screencontroller with a ScalingViewport which stretches the ScreenElements on
     * resize
     *
     * @param batch the batch which should be used to draw with
     */
    public LockpickHUD(SpriteBatch batch) {
        super(batch);
    }

    public LockpickHUD() {
        this(new SpriteBatch());
    }

    public void createLockpickHUD(int size) {
        for(int i = 0; i < size; i++) {
            int xPos = i%4;
            int yPos = i/4;
            ScreenImage img = new ScreenImage("hud/lockpickHud/puzzleButton.png",new Point(0,0));
            img.setPosition(
                ((Constants.WINDOW_WIDTH) / 2.6f - img.getWidth()+xPos*64),
                ((Constants.WINDOW_HEIGHT) / 1.25f + img.getHeight()-yPos*64),
                Align.center | Align.bottom);
            add((T)img);
        }
    }

    /** shows the Menu */
    public void showMenu() {
        this.forEach((Actor s) -> s.setVisible(true));
    }

    /** hides the Menu */
    public void hideMenu() {
        this.forEach((Actor s) -> s.setVisible(false));
    }

    /** Remove the current LockpickHUD **/
    public void removeHUD() {
        forEach(this::remove);
    }
}
