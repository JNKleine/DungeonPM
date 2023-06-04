package ecs.items.IOnUseBehavior;

import ecs.components.HealthComponent;
import ecs.components.InventoryComponent;
import ecs.damage.Damage;
import ecs.damage.DamageType;
import ecs.entities.Entity;
import ecs.entities.Faction;
import ecs.items.IOnUse;
import ecs.items.ItemData;
import starter.Game;
import java.util.Set;

/**
 * To be used for the damagestone to make damage on all enemy entitites in the level
 */
public abstract class DamageFoeMagicOnUse implements IOnUse {
    @Override
    public void onUse(Entity e, ItemData item) {
        Set<Entity>  entities = Game.getEntities();
        for(Entity en : entities) {
            if(en.getFaction().equals(Faction.FOE) || en.getFaction().equals(Faction.BOSSMONSTER)) {
                HealthComponent hc = (HealthComponent)en.getComponent(HealthComponent.class).get();
                hc.receiveHit(new Damage(getDamageAmount(),DamageType.MAGIC,e));
            }
        }
        InventoryComponent ic = (InventoryComponent) e.getComponent(InventoryComponent.class).get();
        ic.removeItem(item);
        ic.setCurMainItem(null);
    }

    public abstract int getDamageAmount();
}
