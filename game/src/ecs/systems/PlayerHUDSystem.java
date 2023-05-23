package ecs.systems;

import ecs.components.HealthComponent;
import ecs.components.InventoryComponent;
import ecs.entities.Hero;
import ecs.items.ItemData;
import starter.Game;

public class PlayerHUDSystem extends ECS_System{
    private HealthComponent heroHC;
    private InventoryComponent heroIC;
    private int currentLiveFromHero;

    private Hero curHero;

    private int moneyOfHero;
    private ItemData curItem;
    public PlayerHUDSystem() {
        heroHC = (HealthComponent) Game.getHero().get().getComponent(HealthComponent.class).get();
        heroIC = (InventoryComponent) Game.getHero().get().getComponent(InventoryComponent.class).get();
        Game.playerHUD.createPlayerHUD(heroHC,heroIC);
        currentLiveFromHero = heroHC.getCurrentHealthpoints();
        curItem = heroIC.getCurMainItem();
        curHero = (Hero) Game.getHero().get();
        moneyOfHero = curHero.getMoney();
    }
    @Override
    public void update() {
        if(heroHC.getCurrentHealthpoints() <= 0) {
            Game.gameOverHUD.showMenu();
        }
        if(heroIC.getCurMainItem() == null && curItem != null) {
            heroIC.setCurMainItemToFirstItemInInventory();
        }
        if(heroHC.getCurrentHealthpoints() != currentLiveFromHero || curItem != heroIC.getCurMainItem() || moneyOfHero != curHero.getMoney()) {
            currentLiveFromHero = heroHC.getCurrentHealthpoints();
            curItem = heroIC.getCurMainItem();
            moneyOfHero = curHero.getMoney();
            Game.playerHUD.removeHUD();
            Game.playerHUD.createPlayerHUD(heroHC,heroIC);
        }
    }
}
