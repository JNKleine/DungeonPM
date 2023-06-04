package ecs.entities;

import dslToGame.AnimationBuilder;
import ecs.components.HealthComponent;
import ecs.components.HitboxComponent;
import ecs.components.IOnDeathFunction;
import ecs.components.ai.AIComponent;
import ecs.components.ai.BossMonsterAI.BossMonsterAI;
import ecs.components.ai.idle.WallWalk;
import ecs.components.ai.transition.RangeTransition;
import graphic.Animation;
import starter.Game;

public class Telepeter extends Monster{

    private final String pathToDieAnim = "character/Telepeter/onDieAnimationLeft";

    private final String pathToGetDamage = "character/Telepeter/onHitAnimationLeft";

    /**
     * Constructor for the telepeter
     **/
    public Telepeter() {
        super(0.02f,0.02f,500,50,0,1,
                "character/Telepeter/idleRight",
                "character/Telepeter/idleLeft",
                "",
                "",
                Faction.BOSSMONSTER);
        addHitBox();
        addAIComponent();
        addHealthComponent();
    }

    private void addHitBox() {
        new HitboxComponent(
            this,
            (you,other,direction) -> System.out.println( "TelepeterCollisionEnter"),
            (you,other,direction) -> System.out.println( "TelepeterCollisionLeave"));
    }

    private void addAIComponent() {
        BossMonsterAI fightAI = new BossMonsterAI(7f);
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
