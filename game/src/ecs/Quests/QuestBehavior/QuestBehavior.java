package ecs.Quests.QuestBehavior;

import ecs.entities.Entity;

public interface QuestBehavior {

    public boolean checkQuestCondition();

    public void increaseProgress();

    public void getReward(Entity e);

    public String getConditionToString();

}
