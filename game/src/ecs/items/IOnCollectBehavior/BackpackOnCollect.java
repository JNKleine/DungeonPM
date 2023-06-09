package ecs.items.IOnCollectBehavior;

import ecs.components.InventoryComponent;
import ecs.components.ItemComponent;
import ecs.entities.Entity;
import ecs.entities.Faction;
import ecs.items.IOnCollect;
import ecs.items.ItemType;
import starter.Game;

/** Handles what happens when a backpack is picked up from the ground */
public class BackpackOnCollect implements IOnCollect {
    private int size = 0;

    /**
     * Creates a new backpack with a given size
     *
     * @param size
     */
    public BackpackOnCollect(int size) {
        this.size = size;
    }

    @Override
    public void onCollect(Entity worldItemEntity, Entity whoCollides) {
        if (whoCollides.getFaction().equals(Faction.PLAYER)) {
            InventoryComponent ic =
                    (InventoryComponent) whoCollides.getComponent(InventoryComponent.class).get();
            if (ic.filledSlots() < ic.getMaxSize() && !ic.backpackIsCollected) {
                ic.addItem(
                        (worldItemEntity
                                .getComponent(ItemComponent.class)
                                .map(ItemComponent.class::cast)
                                .get()
                                .getItemData()));
                Game.removeEntity(worldItemEntity);
                ic.addBackpack(size, ItemType.Active);
            }
        }
    }
}
