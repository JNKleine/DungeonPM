package ecs.Quests.QuestBehavior;

import ecs.entities.Entity;

public interface QuestBehavior {

    /** Checks whether the objective of the quest has been fulfilled * */
    boolean checkQuestCondition();

    /** Increase the progress from this specific quest * */
    void increaseProgress();

    /**
     * The entity get the reward for this quest
     *
     * @param e: Entity, that will be rewarded *
     */
    void getReward(Entity e);

    /** Get the Quest condition as String * */
    String getConditionToString();
}
