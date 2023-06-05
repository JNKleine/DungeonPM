package ecs.Quests.QuestBehavior;

import ecs.entities.Entity;
import ecs.entities.Hero;;

public class KillFoeQuestBehavior implements QuestBehavior {
    private int counter = 0;
    private int questCondition = -1;

    public KillFoeQuestBehavior(int questCondition) {
        counter = 0;
        this.questCondition = questCondition;
    }

    @Override
    public boolean checkQuestCondition() {
        return counter >= questCondition;
    }

    public void increaseProgress() {
        counter++;
    }
    @Override
    public void getReward(Entity e) {
        Hero h = (Hero)e;
        ((Hero) e).increaseMoney(200);
    }

    @Override
    public String getConditionToString() {
        return counter+"/"+questCondition;
    }
}
