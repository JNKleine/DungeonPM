package graphic.hud;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Align;
import controller.ScreenController;
import ecs.components.HealthComponent;
import ecs.components.InventoryComponent;
import tools.Constants;
import tools.Point;

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

    public void createPlayerHUD(HealthComponent hc, InventoryComponent ic) {
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

    public void removeHUD() {
        forEach(this::remove);
    }

    public PlayerHUD() {
        this(new SpriteBatch());
    }


}
