package ecs.entities;

import dslToGame.AnimationBuilder;
import ecs.components.*;
import ecs.components.ai.AIComponent;
import ecs.components.ai.fight.CollideAI;
import ecs.components.ai.idle.StaticRadiusWalk;
import ecs.components.ai.transition.RangeTransition;
import graphic.Animation;
import starter.Game;

/** SlimeGuard is a specific monster of the dungeon. SlimeGuard inherits from Monster */
public class SlimeGuard extends Monster {

    // String path to folders with specific animations
    private final String pathToGetDamage = "monster/slimeGuard/onHitAnimation";
    private final String pathToDieAnim = "monster/slimeGuard/onDieAnimation";

    /**
     * Create an object of type SlimeGuard
     *
     * <p>Within this constructor, all defined and required values are passed to the Monster
     * superclass. In addition, SlimeGuard receives specific components here that were not generally
     * received in monsters.
     */
    public SlimeGuard() {
        super(
                0.05f,
                0.05f,
                40 + (Game.currentLevelNumber / 10),
                2 + (Game.currentLevelNumber / 10),
                0.2f,
                3,
                "monster/slimeGuard/idleRight",
                "monster/slimeGuard/idleLeft",
                "monster/slimeGuard/runRight",
                "monster/slimeGuard/runLeft",
                Faction.FOE);
        addHitBox();
        addAIComponent();
        addHealthComponent();
    }

    // Add HitBox
    private void addHitBox() {
        new HitboxComponent(
                this,
                (you, other, direction) -> System.out.println("SlimeGuardCollisionEnter"),
                (you, other, direction) -> System.out.println("SlimeGuardCollisionLeave"));
    }

    // Add HealthComponent
    private void addHealthComponent() {
        Animation onHit = AnimationBuilder.buildAnimation(this.pathToGetDamage);
        Animation onDeath = AnimationBuilder.buildAnimationNotRepeatable(this.pathToDieAnim);
        new HealthComponent(this, super.initHitpoints, Game::removeEntity, onHit, onDeath);
    }

    // Add a AIComponent with specific AI's
    private void addAIComponent() {
        CollideAI fightAI = new CollideAI(1f);
        StaticRadiusWalk idleAI = new StaticRadiusWalk(5, 4);
        RangeTransition transitionAI = new RangeTransition(4f);

        new AIComponent(this, fightAI, idleAI, transitionAI);
    }
}
