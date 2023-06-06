package ecs.Quests.QuestBehavior;

import ecs.entities.Entity;
import ecs.entities.Hero;;

public class KillFoeQuestBehavior implements QuestBehavior {
    private int counter = 0;
    private int questCondition = -1;

    /**
     * Construct the kill foe quest behavior
     * @param questCondition the number of foes that has to be killed for fulfilling the quest
     */
    public KillFoeQuestBehavior(int questCondition) {
        counter = 0;
        this.questCondition = questCondition;
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
     * The entity gets the reward for this quest
     * @param e: Entity, that will be rewarded
     * **/
    @Override
    public void getReward(Entity e) {
        Hero h = (Hero)e;
        ((Hero) e).increaseMoney(200);
    }

    /**
     * Get the Quest condition as String
     * **/
    @Override
    public String getConditionToString() {
        return counter+"/"+questCondition;
    }
}
