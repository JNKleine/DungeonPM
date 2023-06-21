package graphic.hud;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.utils.Align;
import controller.ScreenController;
import ecs.entities.Entity;
import java.util.HashMap;
import java.util.Random;
import tools.Constants;
import tools.Point;

/** LockPickHUD is for creating a HUD for lockpicking * */
public class LockPickHUD<T extends Actor> extends ScreenController<T> {
    /** Index from the clicked empty field* */
    public int indexEmptyFieldClicked = -1;
    /** Index from the clicked number* */
    public int indexNumberClicked = -1;
    /** Entity, with this lockPickingHUD* */
    public Entity toOpen = null;

    /** Determines, what happen, if a button is clicked * */
    public TextButtonListener tb =
            new TextButtonListener() {
                /** Check, which button is clicked * */
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    if (event.getListenerActor().getName().equals("-1")) {
                        indexEmptyFieldClicked = buttons.get(event.getListenerActor().getName());
                    } else {
                        indexNumberClicked = buttons.get(event.getListenerActor().getName());
                    }
                }
            };

    private HashMap<String, Integer> buttons = new HashMap<>();
    int[] numbers = new int[] {1, 2, 3, 4, 5, 6, 7, 8, -1};

    /**
     * Creates a Screencontroller with a ScalingViewport which stretches the ScreenElements on
     * resize
     *
     * @param batch the batch which should be used to draw with
     */
    public LockPickHUD(SpriteBatch batch) {
        super(batch);
    }

    /** Create a new Object from type lockPickHUD * */
    public LockPickHUD() {
        this(new SpriteBatch());
    }

    /**
     * Create the LockPickHUD and determines where the numbers are
     *
     * @param newHUDToShow: when true: HUD with a new set of numbers will be created when false: HUD
     *     with existing set of numbers will be created *
     */
    public void createLockPickHUD(boolean newHUDToShow) {
        if (newHUDToShow) changeValues();
        for (int i = 0; i < 9; i++) {
            int xPos = i % 3;
            int yPos = i / 3;
            ScreenImage img = new ScreenImage("hud/lockpickHud/puzzleButton.png", new Point(0, 0));
            img.setPosition(
                    ((Constants.WINDOW_WIDTH) / 2.6f - img.getWidth() + xPos * 64),
                    ((Constants.WINDOW_HEIGHT) / 1.25f + img.getHeight() - yPos * 64),
                    Align.center | Align.bottom);
            add((T) img);

            ScreenImage nImg =
                    new ScreenImage(
                            "hud/lockpickHud/numbers/" + numbers[i] + ".png", new Point(0, 0));
            nImg.setPosition(
                    ((Constants.WINDOW_WIDTH) / 2.6f - img.getWidth() + xPos * 64),
                    ((Constants.WINDOW_HEIGHT) / 1.25f + img.getHeight() - yPos * 64),
                    Align.center | Align.bottom);
            add((T) nImg);

            ScreenButton bt = new ScreenButton("", new Point(img.getX(), img.getY()), tb);
            bt.setPosition(img.getX(), img.getY(), Align.center | Align.bottom);
            bt.setName(numbers[i] + "");
            bt.setWidth(32f);
            bt.setHeight(32f);
            add((T) bt);
            buttons.put(bt.getName(), i);
        }
    }

    /** shows the Menu */
    public void showMenu() {
        this.forEach((Actor s) -> s.setVisible(true));
    }

    /** Remove the current LockpickHUD * */
    public void removeHUD() {
        forEach(this::remove);
    }

    /** Randomly shifting the numbers 1 to 8 and -1 in the instance variable of type Int Array * */
    private void changeValues() {
        int value;
        int index;
        Random rd = new Random();
        for (int i = 0; i < numbers.length; i++) {
            index = rd.nextInt(numbers.length);
            value = numbers[i];
            numbers[i] = numbers[index];
            numbers[index] = value;
        }
    }

    /**
     * Swap the value at index position a with the value at position b
     *
     * @param a: Index from Value one
     * @param b: Index from Value two *
     */
    public void swapValues(int a, int b) {
        int valueAtIndexA = numbers[a];
        numbers[a] = numbers[b];
        numbers[b] = valueAtIndexA;
    }

    /** When the puzzle is solved, return true, else false * */
    public boolean checkCondition() {
        for (int i = 0; i < numbers.length - 1; i++) {
            if (!(numbers[i] == i + 1)) {
                return false;
            }
        }
        return true;
    }
}
