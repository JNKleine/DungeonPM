
package ecs.entities;

import dslToGame.AnimationBuilder;
import ecs.components.HealthComponent;
import ecs.components.HitboxComponent;
import ecs.components.IOnDeathFunction;
import ecs.components.ai.AIComponent;
import ecs.components.ai.fight.CollideAI;
import ecs.components.ai.idle.PatrouilleWalk;
import ecs.components.ai.transition.RangeTransition;
import graphic.Animation;
import starter.Game;

import static ecs.components.ai.idle.PatrouilleWalk.MODE.RANDOM;

/**
 * PillowOfBadDreams is a specific monster of the dungeon. PillowOfBadDreams inherits from Monster
 * */
public class PillowOfBadDreams extends Monster{

    //String path to Animations
    private final String pathToGetDamage = "monster/pillowOfBadDreams/onHitAnimation";
    private  final String pathToDieAnim = "monster/pillowOfBadDreams/onDieAnimation";

    /**
     * Create an object of type PillowOfBadDreams
     *
     * <p>Within this constructor, all defined and required values
     * are passed to the Monster superclass.
     * In addition, PillowOfBadDreams receives specific components here that were not
     * generally received in monsters.</p>
     * */
    public PillowOfBadDreams() {
        super(0.1f, 0.1f, 20+(Game.currentLevelNumber/10),
            2+(Game.currentLevelNumber/10),0.1f,2,
            "monster/pillowOfBadDreams/idleRight","monster/pillowOfBadDreams/idleLeft",
            "monster/pillowOfBadDreams/runRight","monster/pillowOfBadDreams/runLeft",Faction.FOE);
        addHitBox();
        addAIComponent();
        addHealthComponent();
    }

//Add hitBox
    private void addHitBox() {
        new HitboxComponent(
            this,
            (you,other,direction) -> System.out.println("PillowOfBadDreamsCollisionEnter"),
            (you,other,direction) -> System.out.println("PillowOfBadDreamsCollisionLeave"));
    }

    //add AIComponent with specific modifiers
    private void addAIComponent() {
        CollideAI fightAI = new CollideAI(2f);
        PatrouilleWalk idleAI = new PatrouilleWalk(5,15,3000,RANDOM);
        RangeTransition transitionAI = new RangeTransition(2f);

        new AIComponent(this, fightAI,idleAI,transitionAI);

    }

    //Add healthComponent
    private void addHealthComponent() {
        Animation onHit = AnimationBuilder.buildAnimation(this.pathToGetDamage);
        Animation onDeath = AnimationBuilder.buildAnimation(this.pathToDieAnim);
        new HealthComponent(this,super.initHitpoints ,(IOnDeathFunction) Game::removeEntity
            ,onHit,onDeath);
    }

}
