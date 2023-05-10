package graphic.hud;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Align;
import controller.ScreenController;
import ecs.systems.DialogueSystem;
import tools.Constants;
import tools.Point;

/**
 *
 * Creates a DialogueMenu for dialogues
 */
public class DialogueMenu  <T extends Actor> extends ScreenController<T> {
    private ScreenInput sI;
    private String userText;

    public static boolean sayIsClicked = false;

    TextButtonListener tb = new TextButtonListener() {
        @Override
        public void clicked(InputEvent event, float x, float y) {
            if(event.getListenerActor().getName().equals("leaveButton")) {
                DialogueSystem.callDialogueHUD("");
            }
            else if(event.getListenerActor().getName().equals("sayButton")) {
                if(sI != null) {
                    sayIsClicked = true;
                    userText = sI.getText();
                }
            }
        }
    };

    /**Creates a DialogueMenu with Spritebatch**/
    public DialogueMenu(SpriteBatch batch) {
        super(batch);
    }

    public DialogueMenu() {
        this(new SpriteBatch());
    }

    /**
     * Creates the HUD for the dialog
     * **/
    public void createDialogueMenu(String answerFromEntity){
        TextButton.TextButtonStyle curFont = new TextButtonStyleBuilder(FontBuilder.DEFAULT_FONT)
            .setFontColor(Color.GRAY).build();

        ScreenImage img = new ScreenImage("hud/inventoryHud/Rahmen.png",new Point(0,0));
        img.setScaleX(15f);
        img.setScaleY(5f);
        img.setPosition(
            ((Constants.WINDOW_WIDTH) / 4.25f - img.getWidth()),
            ((Constants.WINDOW_HEIGHT) / 3f + img.getHeight()),
            Align.center | Align.bottom);
        add((T)img);

        ScreenText answerText = new ScreenText(answerFromEntity,new Point(0,0),1f,
            new LabelStyleBuilder(FontBuilder.DEFAULT_FONT)
                .setFontcolor(Color.GRAY)
                .build());
        answerText.setPosition(
            ((Constants.WINDOW_WIDTH) / 1.5f - answerText.getWidth()),
            ((Constants.WINDOW_HEIGHT) / 1.63f + answerText.getHeight()),
            Align.topLeft | Align.topLeft);
        add((T)answerText);
        ScreenButton sb = new ScreenButton("Leave Dialogue",new Point(0,0),tb,curFont);
        sb.setName("leaveButton");
        sb.setPosition(
            ((Constants.WINDOW_WIDTH) / 2f - sb.getWidth()),
            ((Constants.WINDOW_HEIGHT) / 1.525f + sb.getHeight()),
            Align.center | Align.bottom);
        add((T)sb);

        ScreenButton say = new ScreenButton("click to say:",new Point(0,0),tb,curFont);
        say.setName("sayButton");
        say.setPosition(
            ((Constants.WINDOW_WIDTH) / 1.72f - say.getWidth()),
            ((Constants.WINDOW_HEIGHT) / 2.8f + say.getHeight()),
            Align.center | Align.bottom);
        add((T)say);

        sI = new ScreenInput("Write something here",new Point(0,0));
        sI.setPosition(
            ((Constants.WINDOW_WIDTH) / 1.1f - sI.getWidth()),
            ((Constants.WINDOW_HEIGHT) / 2.8f + sI.getHeight()),
            Align.center | Align.bottom);
        add((T)sI);
    }

    /**
     * Deletes the DialogHud
     * **/
    public void removeDialogueMenu() {
        this.forEach((Actor s) -> s.remove());
    }

    /**
     * Get the input from the player in dialog hud
     * **/
    public String getUserText() {
        return userText;
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
