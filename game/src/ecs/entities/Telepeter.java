package ecs.entities;

import dslToGame.AnimationBuilder;
import ecs.components.HealthComponent;
import ecs.components.HitboxComponent;
import ecs.components.IOnDeathFunction;
import ecs.components.ai.AIComponent;
import ecs.components.ai.BossMonsterAI.BossMonsterAI;
import ecs.components.ai.fight.MeleeAI;
import ecs.components.ai.idle.TelepeterOutOfRangeWalk;
import ecs.components.ai.idle.WallWalk;
import ecs.components.ai.transition.RangeTransition;
import ecs.components.skill.Skill;
import ecs.damage.Damage;
import ecs.damage.DamageType;
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
            "character/Telepeter/idleRight",
            "character/Telepeter/idleRight",
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
        HealthComponent hcH =  (HealthComponent) Game.getHero().get().getComponent(HealthComponent.class).get();
        MeleeAI fightAI = new MeleeAI(1f,new Skill(entity -> hcH.receiveHit(new Damage(getDamage(), DamageType.PHYSICAL,entity)),3f));
        TelepeterOutOfRangeWalk idleAI =  new TelepeterOutOfRangeWalk(90,120,2);
        RangeTransition transitionAI = new RangeTransition(3f);

        new AIComponent(this, fightAI,idleAI,transitionAI);

    }

    private void addHealthComponent() {
        Animation onHit = AnimationBuilder.buildAnimation(this.pathToGetDamage);
        Animation onDeath = AnimationBuilder.buildAnimation(this.pathToDieAnim);
        new HealthComponent(this,super.initHitpoints ,(IOnDeathFunction) Game::removeEntity
            ,onHit,onDeath);
    }
}
