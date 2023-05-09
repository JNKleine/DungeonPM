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

/**
 * To create the ItemHUD with the current items stored in the hero's inventory
 *
 */
public class InventoryMenu <T extends Actor> extends ScreenController<T>  {

    //All button Names with their linked Item
    private HashMap<String,ItemData> buttons = new HashMap<>();
    //List of current Items
    private ArrayList<T> currentInventory = new ArrayList<>();

    //InventoryComponent from Hero
    private InventoryComponent ic;

    /**ButtonListener for the InventoryMenu**/
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

    /** Constructor creates an InventoryMenu with Spritebatch**/
    public InventoryMenu() {
        this(new SpriteBatch());
    }

    public InventoryMenu(SpriteBatch batch) {
        super(batch);
    }


    /**
     * Create Inventory
     * **/
    public void createInventory() {
        createInventoryHUD();
        createItemsInInventory();
    }
    private void createInventoryHUD() {
        Hero curHero = (Hero)Game.getHero().get();
        ic = (InventoryComponent)curHero.getComponent(InventoryComponent.class).get();
        for(int i = 0; i < ic.maxSize; i++) {
            int xPos = i%4;
            int yPos = i/4;
            ScreenImage img = new ScreenImage("hud/inventoryHud/Rahmen.png",new Point(0,0));
            img.setPosition(
                ((Constants.WINDOW_WIDTH) / 2.6f - img.getWidth()+xPos*64),
                ((Constants.WINDOW_HEIGHT) / 1.5f + img.getHeight()-yPos*64),
                Align.center | Align.bottom);
            add((T)img);
            currentInventory.add((T)img);
        }
    }

    private void createItemsInInventory() {
        Hero curHero =(Hero) Game.getHero().get();
        InventoryComponent ic = (InventoryComponent) curHero.getComponent(InventoryComponent.class).get();
        ArrayList<ItemData> items = (ArrayList)ic.getItems();
        for(int i = 0; i < items.size(); i++) {
            int xPos = i%4;
            int yPos = i/4;
            ScreenImage img = new ScreenImage(items.get(i).getInventoryTexture().getNextAnimationTexturePath(),new Point(0,0));
            img.setPosition(
                ((Constants.WINDOW_WIDTH) / 2.6f - img.getWidth()+xPos*64),
                ((Constants.WINDOW_HEIGHT) / 1.5f + img.getHeight()-yPos*64),
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

    /**Remove all elements from the inventory **/
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
