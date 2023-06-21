package ecs.items.IOnUseBehavior;

import ecs.components.HealthComponent;
import ecs.components.InventoryComponent;
import ecs.entities.Entity;
import ecs.items.IOnUse;
import ecs.items.ItemData;

/** Heals a player after using a heal potion */
public class HealPlayerOnUse implements IOnUse {
    private int hp = 0;

    /**
     * @param amountRegenerateHP to specify the amount to heal
     */
    public HealPlayerOnUse(int amountRegenerateHP) {
        hp = amountRegenerateHP;
    }

    @Override
    public void onUse(Entity e, ItemData item) {
        HealthComponent hc = (HealthComponent) e.getComponent(HealthComponent.class).get();
        hc.setCurrentHealthpoints(hc.getCurrentHealthpoints() + hp);
        InventoryComponent ic = (InventoryComponent) e.getComponent(InventoryComponent.class).get();
        ic.removeItem(item);
        ic.setCurMainItem(null);
    }
}
