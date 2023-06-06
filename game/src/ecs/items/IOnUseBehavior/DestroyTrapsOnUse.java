package ecs.items.IOnUseBehavior;

import ecs.components.HealthComponent;
import ecs.components.InventoryComponent;
import ecs.entities.Entity;
import ecs.entities.Faction;
import ecs.items.IOnUse;
import ecs.items.ItemData;
import starter.Game;

/**
 * IOnUse Behavior of the Item Potion of Trap Destroying
 * **/
public class DestroyTrapsOnUse implements IOnUse {
    //Amount of damage the user get
    private int damage = 5;
    /**
     * Determines what happens when the Item is used with this Behavior
     * @param e: Entity that uses the item
     * @param item: itemData from this item
     * **/
    @Override
    public void onUse(Entity e, ItemData item) {
        HealthComponent hc = (HealthComponent) e.getComponent(HealthComponent.class).get();
        hc.setCurrentHealthpoints(hc.getCurrentHealthpoints()-damage);
        InventoryComponent ic = (InventoryComponent) e.getComponent(InventoryComponent.class).get();
        ic.removeItem(item);
        ic.setCurMainItem(null);

        for(Entity entity : Game.getEntities()) {
            if(entity.getFaction().equals(Faction.TRAP)) {
                HealthComponent hcT = (HealthComponent)entity.getComponent(HealthComponent.class).get();
                hcT.setCurrentHealthpoints(0);
            }
        }
    }
}
