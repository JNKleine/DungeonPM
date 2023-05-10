
package ecs.entities;

import dslToGame.AnimationBuilder;
import ecs.components.*;
import ecs.damage.Damage;
import ecs.damage.DamageType;
import graphic.Animation;
import starter.Game;

import java.util.Random;

/**
 * The Gravestone appears together with the Ghost (NPC) in the middle of the game.
 * If the Hero (player character) reaches the Gravestone
 * and the Ghost is close to him, the Hero will be rewarded or punished.
 * It's entity in the ECS.
 * This class helps to set up the gravestone
 * with all its components and attributes .
 */
public class Gravestone extends Entity {

    private final String pathToIdle = "/character/objects/gravestone";

    /** creates an Entity Gravestone with given Components */
    public Gravestone() {
        super(0,Faction.NEUTRAL);
        addPositionComponent();
        addInteractionComponent();
        addAnimationComponent();
        addHitboxComponent();
    }

    //add PositionComponent
    private void addPositionComponent() {
        new PositionComponent(this);
    }

    private void addInteractionComponent() {

    }

    //add HitBoxComponent
    private void addHitboxComponent() {
        new HitboxComponent(
            this,
            (you,other,direction) -> System.out.println("GravestoneCollisionEnter"),
            (you,other,direction) -> System.out.println("GravestoneCollisionLeave"));
    }
    //add AnimationComponent
    private void addAnimationComponent() {
        Animation idle = AnimationBuilder.buildAnimation(this.pathToIdle);
        new AnimationComponent(this,idle,idle);
    }

    /**
     * Determine what should happen when this entity collides with another
     *
     * @param hb HitBoxComponent from other entity
     * */
    @Override

    public void onHit(HitboxComponent hb) {
        Entity e = hb.getEntity();
        if(e.getFaction().equals(Faction.PLAYER)) {
        Random random = new Random();
        int value = random.nextInt(0,2);
        HealthComponent hc = (HealthComponent) e.getComponent(HealthComponent.class).get();
            if(value == 0) {
                hc.setCurrentHealthpoints(hc.getMaximalHealthpoints());
                System.out.println("You have luck!");
            }
            else {
                hc.receiveHit(new Damage(5,DamageType.MAGIC,this));
                System.out.println("You were punished!");
            }
            removeComponent(HitboxComponent.class);
        }
    }

}
