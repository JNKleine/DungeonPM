package ecs.entities.Trap;

import ecs.components.AnimationComponent;
import ecs.components.HealthComponent;
import ecs.components.HitboxComponent;
import ecs.damage.Damage;
import ecs.damage.DamageType;
import ecs.entities.Entity;
import ecs.entities.Faction;

import java.util.logging.Logger;

public class SpikeTrap extends Trap{
    public SpikeTrap() {
        super(20, "objects/traps/spikeTrap/idle",
            "objects/traps/spikeTrap/onActivation",0.4f,3);
    }

    public void onHit(HitboxComponent hb) {
        Entity e = hb.getEntity();
        if (e.getFaction().equals(Faction.PLAYER) || e.getFaction().equals(Faction.FOE)) {
            HealthComponent hc = (HealthComponent) e.getComponent(HealthComponent.class).get();
            hc.receiveHit(new Damage(getDamage(),
                DamageType.PHYSICAL, this));
            Logger.getLogger("DamageTroughTrap");
            HealthComponent ownHC = (HealthComponent) this.getComponent(HealthComponent.class).get();
            ownHC.setCurrentHealthpoints(0);
        }
    }
}
