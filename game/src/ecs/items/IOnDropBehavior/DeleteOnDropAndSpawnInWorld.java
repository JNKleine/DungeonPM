package ecs.items.IOnDropBehavior;

import ecs.components.InventoryComponent;
import ecs.entities.Entity;
import ecs.items.IOnDrop;
import ecs.items.ItemData;
import ecs.items.ItemDataGenerator;
import ecs.items.WorldItemBuilder;
import tools.Point;

/**
 * Handles what happend when an item is dropped out of an inventory and needs to get spawned in the world
 */
public class DeleteOnDropAndSpawnInWorld implements IOnDrop {

    @Override
    public void onDrop(Entity user, ItemData which, Point position) {
        InventoryComponent ic = (InventoryComponent)user.getComponent(InventoryComponent.class).get();
        ic.removeItem(which);
        ic.setCurMainItem(null);
         new WorldItemBuilder().buildWorldItemAtPosition(which,position);
    }
}
