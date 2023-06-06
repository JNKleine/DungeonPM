package graphic.hud;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Align;
import controller.ScreenController;
import ecs.components.HealthComponent;
import ecs.components.InventoryComponent;
import ecs.components.QuestLogComponent;
import ecs.entities.Hero;
import starter.Game;
import tools.Constants;
import tools.Point;

/**
 * Shows graphically the current health points of the associated Entity (player)
 * in the bottom left corner
 */
public class PlayerHUD <T extends Actor> extends ScreenController<T> {
    /**
     * Creates a Screencontroller with a ScalingViewport which stretches the ScreenElements on
     * resize
     *
     * @param batch the batch which should be used to draw with
     */
    public PlayerHUD(SpriteBatch batch) {
        super(batch);
    }

    public PlayerHUD() {
        this(new SpriteBatch());
    }

    /**
     * Creates a PlayerHUD for the associated Entity (the player)
     *
     * @param hc the HealthComponent of the associated Entity
     * @param ic the InteractionComponent of the associated Entity
     * @param qLC the QuestLogComponent of the associated Entity
     **/
    public void createPlayerHUD(HealthComponent hc, InventoryComponent ic, QuestLogComponent qLC) {
        ScreenText currentHP =
            new ScreenText(
                hc.getCurrentHealthpoints()+"/"+hc.getMaximalHealthpoints()+"HP",
                new Point(0, 0),
                1,
                new LabelStyleBuilder(FontBuilder.DEFAULT_FONT)
                    .setFontcolor(hc.getCurrentHealthpoints() >= 20?Color.LIGHT_GRAY:Color.RED)
                    .build());
        currentHP.setFontScale(2);
        add((T) currentHP);

        ScreenText currentQuest =
            new ScreenText(
                qLC.getMainQuest() != null? qLC.getMainQuest().getShortQuestDescriptionWithProgress():"",
                new Point(20, 450),
                1,
                new LabelStyleBuilder(FontBuilder.DEFAULT_FONT)
                    .setFontcolor(Color.LIGHT_GRAY)
                    .build());
        add((T) currentQuest);

        Hero hero = (Hero) Game.getHero().get();
        ScreenImage currMoneyImg = null;
        if ( hero.getMoney() <5)
            currMoneyImg = new ScreenImage("items/coins/coinI/bronzeCoin.png", new Point(0,5));
        else if ( hero.getMoney() >= 5 && hero.getMoney() <10)
            currMoneyImg = new ScreenImage("items/coins/coinV/silverCoin.png", new Point(0,5));
        else if ( hero.getMoney() >= 10 )
            currMoneyImg = new ScreenImage("items/coins/coinX/GoldCoin.png", new Point(0,5));
        ScreenText currMoney =
            new ScreenText(hero.getMoney() + "" ,
                new Point(50,25),
                1,
                new LabelStyleBuilder(FontBuilder.DEFAULT_FONT).setFontcolor(Color.WHITE).build());
        currMoney.setFontScale(2);
        add((T) currMoneyImg);
        add((T) currMoney);


        ScreenImage img = new ScreenImage("hud/inventoryHud/Rahmen.png",new Point(0,0));
        img.setPosition(
            ((Constants.WINDOW_WIDTH) / 1.152f - img.getWidth()+64),
            ((Constants.WINDOW_HEIGHT) / 1.101f + img.getHeight()-64),
            Align.center | Align.bottom);
        add((T)img);

        if(ic.getCurMainItem() != null) {
            ScreenImage curItemImg = new ScreenImage(ic.getCurMainItem().getInventoryTexture().getNextAnimationTexturePath(),
                new Point(0,0));
            curItemImg.setPosition(
                ((Constants.WINDOW_WIDTH) / 1.152f - img.getWidth()+64),
                ((Constants.WINDOW_HEIGHT) / 1.101f + img.getHeight()-64),
                Align.center | Align.bottom);
            add((T)curItemImg);
        }
    }

    /** Removes the PlayerHUD */
    public void removeHUD() {
        forEach(this::remove);
    }
}
