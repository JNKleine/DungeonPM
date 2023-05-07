package ecs.items.IOnDropBehavior;

import ecs.components.InventoryComponent;
import ecs.entities.Entity;
import ecs.items.IOnDrop;
import ecs.items.ItemData;
import tools.Point;

public class DeleteOnDrop implements IOnDrop {
    @Override
    public void onDrop(Entity user, ItemData which, Point position) {
        InventoryComponent ic = (InventoryComponent)user.getComponent(InventoryComponent.class).get();
        ic.removeItem(which);
    }
}
