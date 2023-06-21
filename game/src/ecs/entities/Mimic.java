package ecs.entities;

import dslToGame.AnimationBuilder;
import ecs.components.*;
import ecs.components.ai.AIComponent;
import ecs.components.ai.fight.MeleeAI;
import ecs.components.ai.idle.IIdleAI;
import ecs.components.ai.idle.MimicIdle;
import ecs.components.ai.transition.RangeTransition;
import ecs.components.skill.Skill;
import ecs.damage.Damage;
import ecs.damage.DamageType;
import graphic.Animation;
import starter.Game;

/** Mimic is a Monster chest, they will attack, if, and only if, the hero interact with them. */
public class Mimic extends Monster {

    /** Shows whether the crate has been interacted with * */
    public boolean interacted = false;
    /** Constructor for any given Monster */
    public Mimic() {

        super(
                0.2f,
                0.2f,
                60,
                4,
                0.05f,
                1,
                "character/monster/mimic/idleRight",
                "character/monster/mimic/idleLeft",
                "character/monster/mimic/runRight",
                "character/monster/mimic/runLeft",
                Faction.FOE);
        addHealthComponent();
        addAIComponent(0f, 0f);
        addInteractionComponent();
    }

    private void addHealthComponent() {
        Animation animation = AnimationBuilder.buildAnimation("objects/treasurechest");
        new HealthComponent(
                this,
                initHitpoints,
                new IOnDeathFunction() {
                    @Override
                    public void onDeath(Entity entity) {
                        PositionComponent pc =
                                (PositionComponent)
                                        (entity.getComponent(PositionComponent.class).get());
                        Chest.createNewChest(pc.getPosition());
                        Game.removeEntity(entity);
                    }
                },
                animation,
                animation);
    }

    /**
     * Adds an AIComponent to the mimic
     *
     * @param attackRange : Range, within the Mimic attack
     * @param transitionRange: Range, within the Mimic transition *
     */
    public void addAIComponent(float attackRange, float transitionRange) {
        HealthComponent hcH =
                (HealthComponent) Game.getHero().get().getComponent(HealthComponent.class).get();
        MeleeAI fightAI =
                new MeleeAI(
                        attackRange,
                        new Skill(
                                entity ->
                                        hcH.receiveHit(
                                                new Damage(
                                                        getDamage(), DamageType.PHYSICAL, entity)),
                                3f));
        IIdleAI idleAI = new MimicIdle();
        RangeTransition transitionAI = new RangeTransition(transitionRange);
        new AIComponent(this, fightAI, idleAI, transitionAI);
    }

    private void addInteractionComponent() {
        new InteractionComponent(this, 1, false, entity -> interacted = true);
    }
}
