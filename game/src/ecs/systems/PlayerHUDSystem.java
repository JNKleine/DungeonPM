package ecs.systems;

import ecs.components.HealthComponent;
import ecs.components.InventoryComponent;
import ecs.items.ItemData;
import starter.Game;

public class PlayerHUDSystem extends ECS_System{
    private HealthComponent heroHC;
    private InventoryComponent heroIC;
    private int currentLiveFromHero;
    private ItemData curItem;
    public PlayerHUDSystem() {
        heroHC = (HealthComponent) Game.getHero().get().getComponent(HealthComponent.class).get();
        heroIC = (InventoryComponent) Game.getHero().get().getComponent(InventoryComponent.class).get();
        Game.playerHUD.createPlayerHUD(heroHC,heroIC);
        currentLiveFromHero = heroHC.getCurrentHealthpoints();
        curItem = heroIC.getCurMainItem();
    }
    @Override
    public void update() {
        if(heroHC.getCurrentHealthpoints() <= 0) {
            Game.gameOverHUD.showMenu();
        }
        if(heroIC.getCurMainItem() == null && curItem != null) {
            heroIC.setCurMainItemToFirstItemInInventory();
        }
        if(heroHC.getCurrentHealthpoints() != currentLiveFromHero || curItem != heroIC.getCurMainItem()) {
            currentLiveFromHero = heroHC.getCurrentHealthpoints();
            curItem = heroIC.getCurMainItem();
            Game.playerHUD.removeHUD();
            Game.playerHUD.createPlayerHUD(heroHC,heroIC);
        }
    }
}
