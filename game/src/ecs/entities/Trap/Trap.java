package ecs.entities.Trap;

import dslToGame.AnimationBuilder;
import ecs.components.*;
import ecs.entities.Entity;
import ecs.entities.Faction;
import graphic.Animation;
import starter.Game;


public class Trap extends Entity {
    private String pathToAlternativeAnimation;
    private String pathToNormalAnimation;
    private int maxTrap;
    private float spawnProb;
    public Trap(int initDamage,String pathToNormalAnimation,String pathToActiveAnimation,float spawnProb, int maxTrapInLevel) {
        super(initDamage,Faction.TRAP);
        addPositionComponent();
        addHitBoxComponent();
        this.pathToNormalAnimation = pathToNormalAnimation;
        this.pathToAlternativeAnimation= pathToActiveAnimation;
        addHealthComponent();
        addAnimationComponent();
        maxTrap = maxTrapInLevel;
        this.spawnProb = spawnProb;
    }

    private void addPositionComponent() {
        new PositionComponent(this);
    }

    public void addHitBoxComponent() {
        new HitboxComponent(
        this,
            (you,other,direction) -> System.out.println("PillowOfBadDreamsCollisionEnter"),
            (you,other,direction) -> System.out.println("PillowOfBadDreamsCollisionLeave"));
    }

    private void addAnimationComponent() {
        Animation idleRight = AnimationBuilder.buildAnimation(pathToNormalAnimation);
        Animation idleLeft = AnimationBuilder.buildAnimation(pathToNormalAnimation);
        new AnimationComponent(this,idleRight,idleLeft);
    }

    private void addHealthComponent() {
        Animation onHit = AnimationBuilder.buildAnimation(this.pathToAlternativeAnimation);
        Animation onDeath = AnimationBuilder.buildAnimationNotRepeatable(this.pathToAlternativeAnimation);
        new HealthComponent(this,2 ,Game::sound
            ,onHit,onDeath);
    }

    public int getMaxTrapInLevel() {
        return maxTrap;
    }

    public float getSpawnProb() {
        return spawnProb;
    }
}
