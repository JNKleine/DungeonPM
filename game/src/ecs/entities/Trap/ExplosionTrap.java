package ecs.entities.Trap;

import ecs.components.AnimationComponent;
import ecs.components.HealthComponent;
import ecs.components.HitboxComponent;
import ecs.damage.Damage;
import ecs.damage.DamageType;
import ecs.entities.Entity;
import ecs.entities.Faction;

import java.util.logging.Logger;

public class ExplosionTrap extends Trap {

    public ExplosionTrap() {
        super(40, "objects/traps/explosionTrap/idle",
            "objects/traps/explosionTrap/onExplosion",0.2f,2);
    }

    public void onHit(HitboxComponent hb) {
        Entity e = hb.getEntity();
        if (e.getFaction().equals(Faction.PLAYER) || e.getFaction().equals(Faction.FOE)) {
            HealthComponent hc = (HealthComponent) e.getComponent(HealthComponent.class).get();
            hc.receiveHit(new Damage(getDamage(),
                DamageType.PHYSICAL, this));
            Logger.getLogger("DamageTroughTrap");
            this.removeComponent(HitboxComponent.class);
            HealthComponent ownHC = (HealthComponent) this.getComponent(HealthComponent.class).get();
            ownHC.setCurrentHealthpoints(0);
        }
    }
}
