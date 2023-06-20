package ecs.items.IOnDropBehavior;

import ecs.components.InventoryComponent;
import ecs.entities.Entity;
import ecs.items.IOnDrop;
import ecs.items.ItemData;
import ecs.items.WorldItemBuilder;
import tools.Point;

/**
 * Drop an item at the given position
 * */
public class DeleteOnDropAndPlaceAtSamePosition implements IOnDrop {

    /**
     * What happens onDrop
     * */
    @Override
    public void onDrop(Entity user, ItemData which, Point position) {
        InventoryComponent ic = (InventoryComponent)user.getComponent(InventoryComponent.class).get();
        ic.removeItem(which);
        ic.setCurMainItem(null);
        new WorldItemBuilder().buildWorldItemAtPosition(which,position);
    }
}
