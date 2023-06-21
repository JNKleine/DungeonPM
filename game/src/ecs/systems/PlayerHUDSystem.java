package ecs.systems;

import ecs.Quests.Quest;
import ecs.components.HealthComponent;
import ecs.components.InventoryComponent;
import ecs.components.QuestLogComponent;
import ecs.entities.Hero;
import ecs.items.ItemData;
import starter.Game;

/**
 * System for managing the PlayerHUD which shows the current health points of the associated Entity
 * (player) and shows the GameOverHUD if necessary
 */
public class PlayerHUDSystem extends ECS_System {
    private HealthComponent heroHC;
    private InventoryComponent heroIC;

    private QuestLogComponent heroQLC;

    private Quest currentMainQuest;
    private String mainQuestDescription = "";
    private int currentLiveFromHero;

    private Hero curHero;

    private int moneyOfHero;
    private ItemData curItem;

    public PlayerHUDSystem() {
        heroHC = (HealthComponent) Game.getHero().get().getComponent(HealthComponent.class).get();
        heroIC =
                (InventoryComponent)
                        Game.getHero().get().getComponent(InventoryComponent.class).get();
        heroQLC =
                (QuestLogComponent)
                        Game.getHero().get().getComponent(QuestLogComponent.class).get();

        Game.playerHUD.createPlayerHUD(heroHC, heroIC, heroQLC);
        currentLiveFromHero = heroHC.getCurrentHealthpoints();
        currentMainQuest = heroQLC.getMainQuest();
        curItem = heroIC.getCurMainItem();
        curHero = (Hero) Game.getHero().get();
        moneyOfHero = curHero.getMoney();
    }

    /** Updates the graphic display of the PlayerHUD */
    @Override
    public void update() {
        heroIC =
                (InventoryComponent)
                        Game.getHero().get().getComponent(InventoryComponent.class).get();
        if (heroHC.getCurrentHealthpoints() <= 0) {
            Game.gameOverHUD.showMenu();
        }
        if (heroIC.getCurMainItem() == null && curItem != null) {
            heroIC.setCurMainItemToFirstItemInInventory();
        }
        if (heroHC.getCurrentHealthpoints() != currentLiveFromHero
                || curItem != heroIC.getCurMainItem()
                || moneyOfHero != curHero.getMoney()
                || (currentMainQuest != heroQLC.getMainQuest()
                        || (heroQLC.getMainQuest() != null
                                && (!heroQLC.getMainQuest()
                                                .getShortQuestDescriptionWithProgress()
                                                .equals(mainQuestDescription)
                                        || heroQLC.getMainQuest().isQuestIsFinished())))) {
            currentLiveFromHero = heroHC.getCurrentHealthpoints();
            curItem = heroIC.getCurMainItem();
            currentMainQuest = heroQLC.getMainQuest();
            if (currentMainQuest != null)
                mainQuestDescription =
                        heroQLC.getMainQuest().getShortQuestDescriptionWithProgress();
            moneyOfHero = curHero.getMoney();
            Game.playerHUD.removeHUD();
            Game.playerHUD.createPlayerHUD(heroHC, heroIC, heroQLC);
        }
    }
}
