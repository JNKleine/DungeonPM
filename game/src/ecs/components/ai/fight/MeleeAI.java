package ecs.components.ai.fight;

import com.badlogic.gdx.ai.pfa.GraphPath;
import ecs.components.ai.AITools;
import ecs.components.skill.Skill;
import ecs.entities.Entity;
import level.elements.tile.Tile;

public class MeleeAI implements IFightAI {
    private final float attackRange;
    private final Skill fightSkill;
    private GraphPath<Tile> path;

    /**
     * Attacks the player if he is within the given range. Otherwise, it will move towards the
     * player.
     *
     * @param attackRange Range in which the attack skill should be executed
     * @param fightSkill Skill to be used when an attack is performed
     */
    public MeleeAI(float attackRange, Skill fightSkill) {
        this.attackRange = attackRange;
        this.fightSkill = fightSkill;
    }

    @Override
    public void fight(Entity entity) {
        fightSkill.reduceCoolDown();
        if (AITools.playerInRange(entity, attackRange)) {
            fightSkill.execute(entity);
        } else {
            path = AITools.calculatePathToHero(entity);
            AITools.move(entity, path);
        }
    }
}
