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

public abstract class DamageFoeMagicOnUse implements IOnUse {
    @Override
    public void onUse(Entity e, ItemData item) {
        Set<Entity>  entities = Game.getEntities();
        for(Entity en : entities) {
            HealthComponent hc = (HealthComponent)en.getComponent(HealthComponent.class).get();
            if(en.getFaction().equals(Faction.FOE)) {
                hc.receiveHit(new Damage(getDamageAmount(),DamageType.MAGIC,e));
            }
        }
        InventoryComponent ic = (InventoryComponent) e.getComponent(InventoryComponent.class).get();
        ic.removeItem(item);
    }

    public abstract int getDamageAmount();
}
