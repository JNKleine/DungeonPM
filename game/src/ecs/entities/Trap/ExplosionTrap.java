package ecs.entities.Trap;

import ecs.components.HealthComponent;
import ecs.components.HitboxComponent;
import ecs.damage.Damage;
import ecs.damage.DamageType;
import ecs.entities.Entity;
import ecs.entities.Faction;

/**
 * ExplosionTraps uses explosives to kill an Entity
 * **/
public class ExplosionTrap extends Trap {

    /**
     * Construct an ExplosionTrap
     * **/
    public ExplosionTrap() {
        super(40, "objects/traps/explosionTrap/idle",
            "objects/traps/explosionTrap/onExplosion",0.2f,2);
    }

    /**
     * Determines what happens, if an Entity collides with this trap
     * @param hb: HitBoxComponent from the other Entity
     * **/
    public void onHit(HitboxComponent hb) {
        Entity e = hb.getEntity();
        if (e.getFaction().equals(Faction.PLAYER) || e.getFaction().equals(Faction.FOE)) {
            entityLogger.info(e.getClass().getSimpleName()+" get damage from "+this.getClass().getSimpleName());
            HealthComponent hc = (HealthComponent) e.getComponent(HealthComponent.class).get();
            hc.receiveHit(new Damage(getDamage(),
                DamageType.PHYSICAL, this));
            this.removeComponent(HitboxComponent.class);
            HealthComponent ownHC = (HealthComponent) this.getComponent(HealthComponent.class).get();
            ownHC.setCurrentHealthpoints(0);
        }
    }
}
