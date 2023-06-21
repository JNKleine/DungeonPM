package ecs.items.IOnDropBehavior;

import ecs.components.InventoryComponent;
import ecs.entities.Entity;
import ecs.items.IOnDrop;
import ecs.items.ItemData;
import tools.Point;

/**
 * Handles what happens when the backpack is dropped out of the inventory can only be dropped when
 * its empty
 */
public class BackpackOnDrop implements IOnDrop {
    @Override
    public void onDrop(Entity user, ItemData which, Point position) {

        InventoryComponent ic =
                (InventoryComponent) user.getComponent(InventoryComponent.class).get();
        if (ic.emptySlots() >= ic.getBackpackSize()) {
            ic.removeItem(which);
            ic.setCurMainItem(null);
            ic.removeBackpack();
        }
    }
}
