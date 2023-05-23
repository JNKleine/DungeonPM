package ecs.items.IOnCollectBehavior;

import ecs.entities.Entity;
import ecs.entities.Faction;
import ecs.entities.Hero;
import ecs.items.IOnCollect;
import starter.Game;

public class CoinOnCollect implements IOnCollect {

    private int value;
    public CoinOnCollect(int value) {
        this.value = value;
    }
    @Override
    public void onCollect(Entity worldItemEntity, Entity whoCollides) {
        if( whoCollides.getFaction().equals(Faction.PLAYER)) {
            Hero hero = (Hero) whoCollides;
            hero.increaseMoney(value);
            Game.removeEntity(worldItemEntity);
        }
    }
}
