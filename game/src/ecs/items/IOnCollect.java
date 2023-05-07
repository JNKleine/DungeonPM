package ecs.items;

import ecs.entities.Entity;

public interface IOnCollect {
    void onCollect(Entity worldItemEntity, Entity whoCollides);
}
