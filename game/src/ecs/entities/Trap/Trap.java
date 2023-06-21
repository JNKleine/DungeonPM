package ecs.entities.Trap;

import dslToGame.AnimationBuilder;
import ecs.components.*;
import ecs.entities.Entity;
import ecs.entities.Faction;
import graphic.Animation;
import starter.Game;

/** The class Trap is the mother of all traps * */
public class Trap extends Entity {
    private String pathToAlternativeAnimation;
    private String pathToNormalAnimation;
    private int maxTrap;
    private float spawnProb;
    /**
     * Construct an object from type Trap
     *
     * @param initDamage initial damage, that the trap gives
     * @param pathToNormalAnimation path to animation, for the inactive trap
     * @param pathToActiveAnimation path to animation for the active trap
     * @param maxTrapInLevel number of maximal possible traps in one level
     * @param spawnProb spawn probability of this trap *
     */
    public Trap(
            int initDamage,
            String pathToNormalAnimation,
            String pathToActiveAnimation,
            float spawnProb,
            int maxTrapInLevel) {
        super(initDamage, Faction.TRAP);
        addPositionComponent();
        addHitBoxComponent();
        this.pathToNormalAnimation = pathToNormalAnimation;
        this.pathToAlternativeAnimation = pathToActiveAnimation;
        addHealthComponent();
        addAnimationComponent();
        maxTrap = maxTrapInLevel;
        this.spawnProb = spawnProb;
    }

    // Add PositionComponent
    private void addPositionComponent() {
        new PositionComponent(this);
    }

    // Add a HitboxComponent to this Trap
    private void addHitBoxComponent() {
        new HitboxComponent(
                this,
                (you, other, direction) -> System.out.println("TrapCollisionEnter"),
                (you, other, direction) -> System.out.println("TrapCollisionLeave"));
    }

    private void addAnimationComponent() {
        Animation idleRight = AnimationBuilder.buildAnimation(pathToNormalAnimation);
        Animation idleLeft = AnimationBuilder.buildAnimation(pathToNormalAnimation);
        new AnimationComponent(this, idleRight, idleLeft);
    }

    private void addHealthComponent() {
        Animation onHit = AnimationBuilder.buildAnimation(this.pathToAlternativeAnimation);
        Animation onDeath =
                AnimationBuilder.buildAnimationNotRepeatable(this.pathToAlternativeAnimation);
        new HealthComponent(this, 2, Game::sound, onHit, onDeath);
    }

    /** Get the maximum number of traps, that should be in a level from this type * */
    public int getMaxTrapInLevel() {
        return maxTrap;
    }
    /** Get the spawn probability of this trap. * */
    public float getSpawnProb() {
        return spawnProb;
    }
}
