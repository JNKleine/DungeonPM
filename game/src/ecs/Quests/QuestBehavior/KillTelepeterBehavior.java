package ecs.Quests.QuestBehavior;

import ecs.components.HealthComponent;
import ecs.entities.Entity;
import ecs.entities.Hero;
import starter.Game;

public class KillTelepeterBehavior implements QuestBehavior{
    private int counter;
    private int questCondition;

    public KillTelepeterBehavior() {
        counter = 0;
        this.questCondition = 1;
    }

    /**
     * Checks whether the objective of the quest has been fulfilled
     * **/
    @Override
    public boolean checkQuestCondition() {
        return counter >= questCondition;
    }

    /**
     * Increase the progress from this specific quest
     * **/
    public void increaseProgress() {
        counter++;
    }

    /**
     * The entity get the reward for this quest
     * @param e: Entity, that will be rewarded
     * **/
    @Override
    public void getReward(Entity e) {
        //Main reward is the dropped Emerald from the Telepeter
        HealthComponent hc = (HealthComponent) Game.getHero().get().getComponent(HealthComponent.class).get();
        hc.setCurrentHealthpoints(hc.getMaximalHealthpoints());
    }

    /**
     * Get the Quest condition as String
     * **/
    @Override
    public String getConditionToString() {
        return counter+"/"+questCondition;
    }
}
