package ecs.entities;

import dslToGame.AnimationBuilder;
import ecs.components.HealthComponent;
import ecs.components.HitboxComponent;
import ecs.components.IOnDeathFunction;
import ecs.components.ai.AIComponent;
import ecs.components.ai.fight.CollideAI;
import ecs.components.ai.idle.WallWalk;
import ecs.components.ai.transition.RangeTransition;
import graphic.Animation;
import starter.Game;

public class BossMonster extends Monster{

    private String name;

    private final String pathToDieAnim;

    private final String pathToGetDamage;
    /**
     * Constructor for a Bossmonster
     *
     * @param initHitpoints         : initial int value for Livepoints
     * @param initDamage            : initial int value for damage per hit
     * @param pathToIdleRight       : String path idle right animation
     * @param pathToIdleLeft        : String path idle left animation
     * @param name                  : Name of the Bossmonster
     **/
    public BossMonster(String name, int initHitpoints, int initDamage, String pathToIdleRight, String pathToIdleLeft, String pathToDieAnim, String pathToGetDamage) {
        super(initHitpoints, initDamage, pathToIdleRight, pathToIdleLeft, Faction.BOSSMONSTER);
        this.name = name;
        this.pathToDieAnim = pathToDieAnim;
        this.pathToGetDamage = pathToGetDamage;
        addHitBox();
        addAIComponent();
        addHealthComponent();
    }

    private void addHitBox() {
        new HitboxComponent(
            this,
            (you,other,direction) -> System.out.println( this.name + "CollisionEnter"),
            (you,other,direction) -> System.out.println( this.name + "CollisionLeave"));
    }

    private void addAIComponent() {
        CollideAI fightAI = new CollideAI(0f);
        // Update to Teleport AI
        WallWalk idleAI = new WallWalk();
        RangeTransition transitionAI = new RangeTransition(0f);

        new AIComponent(this, fightAI,idleAI,transitionAI);
    }

    private void addHealthComponent() {
        Animation onHit = AnimationBuilder.buildAnimation(this.pathToGetDamage);
        Animation onDeath = AnimationBuilder.buildAnimation(this.pathToDieAnim);
        new HealthComponent(this,super.initHitpoints ,(IOnDeathFunction) Game::removeEntity
            ,onHit,onDeath);
    }
}
