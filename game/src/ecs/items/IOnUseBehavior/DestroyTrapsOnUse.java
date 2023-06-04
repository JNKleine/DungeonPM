package ecs.items.IOnUseBehavior;

import ecs.components.HealthComponent;
import ecs.components.InventoryComponent;
import ecs.entities.Entity;
import ecs.entities.Faction;
import ecs.items.IOnUse;
import ecs.items.ItemData;
import starter.Game;

public class DestroyTrapsOnUse implements IOnUse {
    private int damage = 5;
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
