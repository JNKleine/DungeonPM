
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

/**
 * WallWalker is a specific monster of the dungeon. WallWalker inherits from Monster
 **/

 public class WallWalker extends Monster {

 private final String pathToGetDamage = "monster/wallWalker/onHitAnimation";
 private  final String pathToDieAnim = "monster/wallWalker/onDieAnimation";

 /**
 * Create an object of type WallWalker
 *
 * <p>Within this constructor, all defined and required values
 * are passed to the Monster superclass.
 * In addition, WallWalker receives specific components here that were not
 * generally received in monsters.</p>
 **/
 public WallWalker() {
 super(0.05f, 0.05f, 20+(Game.currentLevelNumber/10),
 1+(Game.currentLevelNumber/10),0.5f,1,
 "monster/wallWalker/idleRight","monster/wallWalker/idleLeft",
 "monster/wallWalker/runRight","monster/wallWalker/runLeft",Faction.FOE);
 addHitBox();
 addAIComponent();
 addHealthComponent();
 }
 //Add hitBox
 private void addHitBox() {
 new HitboxComponent(
 this,
 (you,other,direction) -> System.out.println("WallWalkerCollisionEnter"),
 (you,other,direction) -> System.out.println("WallWalkerCollisionLeave"));
 }
 //Add AIComponent
 private void addAIComponent() {
 CollideAI fightAI = new CollideAI(0f);
 WallWalk idleAI = new WallWalk();
 RangeTransition transitionAI = new RangeTransition(0f);

 new AIComponent(this, fightAI,idleAI,transitionAI);

 }


    //Add healthComponent
    private void addHealthComponent() {
        Animation onHit = AnimationBuilder.buildAnimation(this.pathToGetDamage);
        Animation onDeath = AnimationBuilder.buildAnimationNotRepeatable(this.pathToDieAnim);
        new HealthComponent(this,super.initHitpoints ,Game::removeEntity
            ,onHit,onDeath);
    }


 }
