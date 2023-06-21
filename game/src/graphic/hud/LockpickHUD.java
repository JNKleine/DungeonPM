package graphic.hud;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.utils.Align;
import controller.ScreenController;
import ecs.entities.Entity;
import tools.Constants;
import tools.Point;
import java.util.HashMap;
import java.util.Random;

public class LockPickHUD <T extends Actor> extends ScreenController<T> {
    public int indexEmptyFieldClicked = -1;
    public int indexNumberClicked = -1;

    public boolean finished = false;

    public Entity toOpen = null;


    public TextButtonListener tb = new TextButtonListener() {
        @Override
        public void clicked(InputEvent event, float x, float y) {
            System.out.println(event.getListenerActor().getName());
            if(event.getListenerActor().getName().equals("-1")) {
                indexEmptyFieldClicked = buttons.get(event.getListenerActor().getName());
            }
            else {
                indexNumberClicked = buttons.get(event.getListenerActor().getName());
            }
        }
    };

    private HashMap<String,Integer> buttons = new HashMap<>();
    int[] intArr = new int[]{1,2,3,4,5,6,7,8,-1};

    /**
     * Creates a Screencontroller with a ScalingViewport which stretches the ScreenElements on
     * resize
     *
     * @param batch the batch which should be used to draw with
     */
    public LockPickHUD(SpriteBatch batch) {
        super(batch);
    }

    public LockPickHUD() {
        this(new SpriteBatch());
    }

    public void createLockPickHUD(boolean newHUDToShow) {
        finished = false;
        if(newHUDToShow)changeValues();
        for(int i = 0; i < 9; i++) {
            int xPos = i%3;
            int yPos = i/3;
            ScreenImage img = new ScreenImage("hud/lockpickHud/puzzleButton.png",new Point(0,0));
            img.setPosition(
                ((Constants.WINDOW_WIDTH) / 2.6f - img.getWidth()+xPos*64),
                ((Constants.WINDOW_HEIGHT) / 1.25f + img.getHeight()-yPos*64),
                Align.center | Align.bottom);
            add((T)img);

            ScreenImage nImg = new ScreenImage("hud/lockpickHud/numbers/"+intArr[i]+".png",new Point(0,0));
            nImg.setPosition(
                ((Constants.WINDOW_WIDTH) / 2.6f - img.getWidth()+xPos*64),
                ((Constants.WINDOW_HEIGHT) / 1.25f + img.getHeight()-yPos*64),
                Align.center | Align.bottom);
            add((T)nImg);

            ScreenButton bt = new ScreenButton("",new Point(img.getX(),img.getY()),tb);
            bt.setPosition(img.getX(),img.getY(),Align.center | Align.bottom);
            bt.setName(intArr[i]+"");
            bt.setWidth(32f);
            bt.setHeight(32f);
            add((T)bt);
            buttons.put(bt.getName(),i);
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

    private void changeValues() {
        int value;
        int index;
        Random rd = new Random();
        for (int i = 0; i < intArr.length; i++) {
            index = rd.nextInt(intArr.length);
            value = intArr[i];
            intArr[i] = intArr[index];
            intArr[index] = value;
    }

}

public void swapValues(int a, int b) {
        int valueAtIndexA = intArr[a];
        intArr[a] = intArr[b];
        intArr[b] = valueAtIndexA;
}

public boolean checkCondition() {
        for(int i = 0; i < intArr.length-1; i++) {
            if(!(intArr[i] == i+1)) {
                return false;
            }
        }
    return true;
}
}
