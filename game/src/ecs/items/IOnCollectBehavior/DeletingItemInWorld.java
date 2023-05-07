package ecs.items.IOnCollectBehavior;

import ecs.components.InventoryComponent;
import ecs.components.ItemComponent;
import ecs.entities.Entity;
import ecs.entities.Faction;
import ecs.items.IOnCollect;
import starter.Game;

public class DeletingItemInWorld implements IOnCollect {
    @Override
    public void onCollect(Entity worldItemEntity, Entity whoCollides) {
        if(whoCollides.getFaction().equals(Faction.PLAYER)) {
            InventoryComponent ic = (InventoryComponent) whoCollides.getComponent(InventoryComponent.class).get();
            if(ic.filledSlots() < ic.getMaxSize()) {
                ic.addItem((worldItemEntity.getComponent(ItemComponent.class).
                    map(ItemComponent.class::cast).get().getItemData()));
                Game.removeEntity(worldItemEntity);
            }
        }
    }
}
