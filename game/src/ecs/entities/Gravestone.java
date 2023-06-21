package ecs.entities;

import dslToGame.AnimationBuilder;
import ecs.components.*;
import ecs.damage.Damage;
import ecs.damage.DamageType;
import graphic.Animation;
import starter.Game;

/**
 * The Gravestone appears together with the Ghost (NPC) in the middle of the game. If the Hero
 * (player character) reaches the Gravestone and the Ghost is close to him, the Hero will be
 * rewarded or punished. It's entity in the ECS. This class helps to set up the gravestone with all
 * its components and attributes .
 */
public class Gravestone extends Entity {

    private final String pathToIdle = "/character/objects/gravestone";

    /** creates an Entity Gravestone with given Components */
    public Gravestone() {
        super(0, Faction.NEUTRAL);
        addPositionComponent();
        addInteractionComponent();
        addAnimationComponent();
        addHitboxComponent();
    }

    // add PositionComponent
    private void addPositionComponent() {
        new PositionComponent(this);
    }

    private void addInteractionComponent() {
        new InteractionComponent(
                this,
                3,
                false,
                new OpenDialogueOnInteraction(
                        this, "What is the Name of this Graves owner?", false));
    }

    // add HitBoxComponent
    private void addHitboxComponent() {
        new HitboxComponent(
                this,
                (you, other, direction) -> System.out.println("GravestoneCollisionEnter"),
                (you, other, direction) -> System.out.println("GravestoneCollisionLeave"));
    }
    // add AnimationComponent
    private void addAnimationComponent() {
        Animation idle = AnimationBuilder.buildAnimation(this.pathToIdle);
        new AnimationComponent(this, idle, idle);
    }

    @Override
    public String getAnswer(String text) {
        Hero player = (Hero) Game.getHero().get();
        HealthComponent hc = (HealthComponent) player.getComponent(HealthComponent.class).get();
        if (text.equalsIgnoreCase(Ghost.getName().toLowerCase())) {
            hc.setCurrentHealthpoints(hc.getMaximalHealthpoints());
            return "This name is known to me, may the undead world\ngive you strength. "
                    + "May the owner of this tomb\nrest forever."
                    + "\nYou gain 20HP!";

        } else {
            hc.receiveHit(new Damage(30, DamageType.MAGIC, this));
            return "Oh no! such a name is not known to me!\nYour lifeblood is cursed!\n30 Damage added";
        }
    }
}
