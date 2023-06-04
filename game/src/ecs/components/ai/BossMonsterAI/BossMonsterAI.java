package ecs.components.ai.BossMonsterAI;

import ecs.components.ai.AIComponent;
import ecs.components.ai.AITools;
import ecs.components.ai.fight.CollideAI;
import ecs.components.ai.fight.IFightAI;
import ecs.entities.Entity;
import starter.Game;

public class BossMonsterAI implements IFightAI {

    private float changeAIRange;
    public BossMonsterAI (float changeAIRange) {
        this.changeAIRange = changeAIRange;
    }
    @Override
    public void fight(Entity entity) {
        if (AITools.playerInRange(entity,changeAIRange) ) {
            AIComponent aiC = (AIComponent) entity.getComponent(AIComponent.class).get();
            aiC.setFightAI(new CollideAI(2f));
        } else {

        }
    }
}
