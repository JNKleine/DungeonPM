package graphic.hud;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Align;
import controller.ScreenController;

import tools.Constants;
import tools.Point;

import java.util.ArrayList;

public class QuestHUD  <T extends Actor> extends ScreenController<T> {
    private ArrayList<String> questText;
    /**
     * Creates a Screencontroller with a ScalingViewport which stretches the ScreenElements on
     * resize
     *
     * @param batch the batch which should be used to draw with
     */
    public QuestHUD(SpriteBatch batch) {
        super(batch);
    }

    public QuestHUD() {
        this(new SpriteBatch());
    }

    public void createQuestHUD() {

            ScreenImage img = new ScreenImage("hud/questHud/questHUD.png",new Point(0,0));
        img.setScaleX(15f);
        img.setScaleY(10f);
            img.setPosition(
                ((Constants.WINDOW_WIDTH) / 3.7f - img.getWidth()),
                ((Constants.WINDOW_HEIGHT) / 3.7f + img.getHeight()),
                Align.center | Align.bottom);
            add((T) img);
            hideMenu();
    }

    public void removeHUD() {
        forEach(this::remove);
    }

    /** shows the Menu */
    public void showMenu() {
        this.forEach((Actor s) -> s.setVisible(true));
    }

    /** hides the Menu */
    public void hideMenu() {
        this.forEach((Actor s) -> s.setVisible(false));
    }
}
