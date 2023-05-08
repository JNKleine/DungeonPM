package ecs.items.IOnCollectBehavior;

import ecs.components.InventoryComponent;
import ecs.components.ItemComponent;
import ecs.entities.Entity;
import ecs.entities.Faction;
import ecs.items.IOnCollect;
import ecs.items.ItemType;
import starter.Game;

public class BackpackOnCollect implements IOnCollect {
private int size = 0;
    public BackpackOnCollect(int size) {
        this.size = size;
    }

    public void onCollect(Entity worldItemEntity, Entity whoCollides) {
        if(whoCollides.getFaction().equals(Faction.PLAYER)) {
            InventoryComponent ic = (InventoryComponent) whoCollides.getComponent(InventoryComponent.class).get();
            if(ic.filledSlots() < ic.getMaxSize() && !ic.backpackIsCollected) {
                ic.addItem((worldItemEntity.getComponent(ItemComponent.class).
                    map(ItemComponent.class::cast).get().getItemData()));
                Game.removeEntity(worldItemEntity);
                ic.addBackpack(size, ItemType.Active);
            }
        }
    }

}
