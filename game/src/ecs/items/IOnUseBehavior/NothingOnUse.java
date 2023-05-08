package ecs.items.IOnUseBehavior;

import ecs.entities.Entity;
import ecs.items.IOnUse;
import ecs.items.ItemData;

public class NothingOnUse implements IOnUse {

    @Override
    public void onUse(Entity e, ItemData item) {
        //Do nothing!
    }
}
