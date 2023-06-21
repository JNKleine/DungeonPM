package ecs.components.ai.idle;

import ecs.components.ai.AITools;
import ecs.entities.Entity;

public class FollowWalk implements IIdleAI {

    /** Walks to the hero. */
    @Override
    public void idle(Entity entity) {
        AITools.move(entity, AITools.calculatePathToHero(entity));
    }
}
