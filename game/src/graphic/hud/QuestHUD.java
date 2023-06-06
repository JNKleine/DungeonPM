package graphic.hud;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.utils.Align;
import controller.ScreenController;

import ecs.Quests.Quest;
import ecs.components.QuestLogComponent;
import ecs.entities.Entity;
import starter.Game;
import tools.Constants;
import tools.Point;

import java.util.ArrayList;
import java.util.HashMap;

public class QuestHUD  <T extends Actor> extends ScreenController<T> {

    public static boolean buttonIsClicked = false;
    public static Quest quest;

    private HashMap<String,Quest> buttons = new HashMap<>();
    QuestLogComponent qlC;

    public TextButtonListener tb = new TextButtonListener() {
        @Override
        public void clicked(InputEvent event, float x, float y) {
            Quest q = buttons.get(event.getListenerActor().getName());
            if(!q.isQuestIsFinished()) {
                qlC.setCurMainQuest(q);
            } else {
                buttonIsClicked = true;
                quest = q;
            }
        }
    };
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

    /** Create the QuestHUD, to show all active Quests **/
    public void createQuestHUD() {
        Entity hero = Game.getHero().get();
        qlC = (QuestLogComponent)hero.getComponent(QuestLogComponent.class).get();

            ScreenImage img = new ScreenImage("hud/questHud/questHUD.png",new Point(0,0));
        img.setScaleX(15f);
        img.setScaleY(10f);
            img.setPosition(
                ((Constants.WINDOW_WIDTH) / 3.7f - img.getWidth()),
                ((Constants.WINDOW_HEIGHT) / 3.7f + img.getHeight()),
                Align.center | Align.bottom);
            add((T) img);

        ScreenText questNumber = new ScreenText(qlC.getQuestList().size()+"/"+qlC.getMaxQuestNumber(),new Point(0,0),1f,
            new LabelStyleBuilder(FontBuilder.DEFAULT_FONT)
                .setFontcolor(Color.GRAY)
                .build());
        questNumber.setPosition(
            ((Constants.WINDOW_WIDTH) / 1.09f - img.getWidth()),
            ((Constants.WINDOW_HEIGHT) / 1.09f + img.getHeight()),
            Align.left | Align.topLeft);
        add((T)questNumber);

            if(qlC.isEmpty()) {
                ScreenText noQuestText = new ScreenText("No active Quests",new Point(0,0),1f,
                    new LabelStyleBuilder(FontBuilder.DEFAULT_FONT)
                        .setFontcolor(Color.GRAY)
                        .build());
                noQuestText.setFontScale(3f);
                noQuestText.setPosition(
                    ((Constants.WINDOW_WIDTH) / 2.88f - img.getWidth()),
                    ((Constants.WINDOW_HEIGHT) / 1.6f + img.getHeight()),
                    Align.left | Align.topLeft);
                add((T)noQuestText);
            } else {
                ArrayList<Quest> questList = qlC.getQuestList();
                for(int i = 0; i < questList.size(); i++) {
                    ScreenText questText = new ScreenText(questList.get(i).getShortQuestDescriptionWithProgress(),
                        new Point(0,0),1f,
                        new LabelStyleBuilder(FontBuilder.DEFAULT_FONT)
                            .setFontcolor(Color.GRAY)
                            .build());
                    questText.setFontScale(1.1f);
                    questText.setPosition(
                        ((Constants.WINDOW_WIDTH) / 2.88f - img.getWidth()),
                        ((Constants.WINDOW_HEIGHT) / 1.13f + img.getHeight()-i*28),
                        Align.left | Align.topLeft);
                    add((T)questText);

                    ScreenButton bt = new ScreenButton("",new Point(questText.getX(),questText.getY()),tb);
                    bt.setWidth(questText.getWidth());
                    bt.setScale(1.1f);
                    bt.setName(i+"");
                    add((T)bt);
                    buttons.put(bt.getName(),questList.get(i));
                }
            }
            hideMenu();
    }

    /** Remove the current QuestHUD **/
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
