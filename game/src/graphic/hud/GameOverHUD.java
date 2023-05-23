package graphic.hud;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.utils.Align;
import controller.ScreenController;
import ecs.components.HealthComponent;
import starter.Game;
import tools.Constants;
import tools.Point;

public class GameOverHUD <T extends Actor> extends ScreenController<T> {

    TextButtonListener tb = new TextButtonListener() {
        @Override
        public void clicked(InputEvent event, float x, float y) {
            if(event.getListenerActor().getName().equals("restartButton")) {
                System.out.println("Restart the Game");
            }
            else if(event.getListenerActor().getName().equals("leaveButton")) {
                System.out.println("Leave the Game");
            }
        }
    };
    /**
     * Creates a Screencontroller with a ScalingViewport which stretches the ScreenElements on
     * resize
     *
     * @param batch the batch which should be used to draw with
     */
    public GameOverHUD(SpriteBatch batch) {
        super(batch);
    }

    public GameOverHUD() {
        this(new SpriteBatch());
        ScreenText screenText =
            new ScreenText(
                "Game Over",
                new Point(0, 0),
                3,
                new LabelStyleBuilder(FontBuilder.DEFAULT_FONT)
                    .setFontcolor(Color.RED)
                    .build());
        screenText.setFontScale(4);
        screenText.setPosition(
            (Constants.WINDOW_WIDTH) / 2f - screenText.getWidth(),
            (Constants.WINDOW_HEIGHT) / 1.5f + screenText.getHeight(),
            Align.center | Align.bottom);
        add((T) screenText);

        ScreenButton restartButton = new ScreenButton("Restart",new Point(0,0),tb,
            new TextButtonStyleBuilder(FontBuilder.DEFAULT_FONT)
                .setFontColor(Color.GREEN)
                .build());
        restartButton.setName("restartButton");
        restartButton.setScale(2);
        restartButton.setPosition(
            (Constants.WINDOW_WIDTH) / 2f - restartButton.getWidth(),
            (Constants.WINDOW_HEIGHT) / 1.7f + restartButton.getHeight(),
            Align.center | Align.bottom);
        add((T)restartButton);

        ScreenButton leaveButton = new ScreenButton("Leave Game",new Point(0,0),tb,
            new TextButtonStyleBuilder(FontBuilder.DEFAULT_FONT)
            .setFontColor(Color.RED)
            .build());
        leaveButton.setName("leaveButton");
        leaveButton.setScale(2);
        leaveButton.setPosition(
            (Constants.WINDOW_WIDTH) / 1.25f - leaveButton.getWidth(),
            (Constants.WINDOW_HEIGHT) / 1.7f + leaveButton.getHeight(),
            Align.center | Align.bottom);
        add((T)leaveButton);
        hideMenu();
    }

    /**shows the Menu**/
    public void showMenu() {
        this.forEach((Actor s) -> s.setVisible(true));
        Game.addRecentWindow(Game.gameOverHUD);
    }

    /** hides the Menu */
    public void hideMenu() {
        this.forEach((Actor s) -> s.setVisible(false));
    }
}
