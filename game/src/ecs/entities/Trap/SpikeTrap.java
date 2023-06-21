package ecs.entities.Trap;

import ecs.components.HealthComponent;
import ecs.components.HitboxComponent;
import ecs.damage.Damage;
import ecs.damage.DamageType;
import ecs.entities.Entity;
import ecs.entities.Faction;

/** SpikeTrap, uses spikes to kill entities * */
public class SpikeTrap extends Trap {
    /** Construct a SpikeTrap * */
    public SpikeTrap() {
        super(20, "objects/traps/spikeTrap/idle", "objects/traps/spikeTrap/onActivation", 0.4f, 3);
    }

    /**
     * Determines what happen, if an Entity collides with this trap
     *
     * @param hb: HitBoxComponent from the other Entity *
     */
    public void onHit(HitboxComponent hb) {
        Entity e = hb.getEntity();
        if (e.getFaction().equals(Faction.PLAYER) || e.getFaction().equals(Faction.FOE)) {
            entityLogger.info(
                    e.getClass().getSimpleName()
                            + " get damage from "
                            + this.getClass().getSimpleName());
            HealthComponent hc = (HealthComponent) e.getComponent(HealthComponent.class).get();
            hc.receiveHit(new Damage(getDamage(), DamageType.PHYSICAL, this));
            HealthComponent ownHC =
                    (HealthComponent) this.getComponent(HealthComponent.class).get();
            ownHC.setCurrentHealthpoints(0);
        }
    }
}
