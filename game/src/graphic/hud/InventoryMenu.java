package graphic.hud;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.utils.Align;
import controller.ScreenController;
import ecs.components.InventoryComponent;
import ecs.entities.Hero;
import ecs.items.ItemData;
import starter.Game;
import tools.Constants;
import tools.Point;

import java.util.ArrayList;
import java.util.HashMap;


public class InventoryMenu <T extends Actor> extends ScreenController<T>  {

    public TextButtonListener tb = new TextButtonListener() {
        @Override
        public void clicked( InputEvent event, float x, float y) {
                Hero curHero = (Hero)Game.getHero().get();
                ItemData id = buttons.get(event.getListenerActor().getName());
                System.out.println(id.getDescription());
                InventoryComponent ic = (InventoryComponent) curHero.getComponent(InventoryComponent.class).get();
                ic.setCurMainItem(id);
        }
    };

    private HashMap<String,ItemData> buttons = new HashMap<>();
    private ArrayList<T> currentInventory = new ArrayList<>();
    public InventoryMenu() {
        this(new SpriteBatch());
    }

    public InventoryMenu(SpriteBatch batch) {
        super(batch);
        createInventoryHUD(4);
        hideMenu();
    }

    private void createInventoryHUD(int size) {
        for(int i = 0; i < size; i++) {
            ScreenImage img = new ScreenImage("hud/inventoryHud/Rahmen.png",new Point(0,0));
            img.setPosition(
                ((Constants.WINDOW_WIDTH) / 2.6f - img.getWidth()+i*64),
                (Constants.WINDOW_HEIGHT) / 1.5f + img.getHeight(),
                Align.center | Align.bottom);
            add((T)img);
        }
    }

    public void createItemsInInventory() {
        Hero curHero =(Hero) Game.getHero().get();
        InventoryComponent ic = (InventoryComponent) curHero.getComponent(InventoryComponent.class).get();
        ArrayList<ItemData> items = (ArrayList)ic.getItems();
        for(int i = 0; i < items.size(); i++) {
            ScreenImage img = new ScreenImage(items.get(i).getInventoryTexture().getNextAnimationTexturePath(),new Point(0,0));
            img.setPosition(
                ((Constants.WINDOW_WIDTH) / 2.6f - img.getWidth()+i*64),
                (Constants.WINDOW_HEIGHT) / 1.5f + img.getHeight(),
                Align.center | Align.bottom);
            add((T)img);

            ScreenButton bt = new ScreenButton(" ",new Point(img.getX(),img.getY()),tb);
            bt.setWidth(32f);
            bt.setHeight(32f);
            bt.setName(i+"");
            add((T)bt);
            currentInventory.add((T)img);
            currentInventory.add((T)bt);
            buttons.put(bt.getName(),items.get(i));
        }
    }

    public void removeInventory() {
       for(int i = 0; i < currentInventory.size(); i++) {
           remove(currentInventory.get(i));
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


}
