package ecs.Quests.QuestBehavior;

import ecs.components.HealthComponent;
import ecs.entities.Entity;
import ecs.entities.Hero;
import starter.Game;

public class CollectCoinsBehavior implements QuestBehavior{
    private int condition;
    public CollectCoinsBehavior(int condition) {
        this.condition = condition;
    }

    /**
     * Checks whether the objective of the quest has been fulfilled
     * **/
    @Override
    public boolean checkQuestCondition() {
        return ((Hero)Game.getHero().get()).getMoney() >= condition;
    }

    /**
     * Since this quest is a collecting quest,
     * increaseProgress do nothing here
     * **/
    @Override
    public void increaseProgress() {
      //Nothing to Increase only checkConditions
    }

    /**
     * The entity get the reward for this quest
     * @param e: Entity, that will be rewarded
     * **/
    @Override
    public void getReward(Entity e) {
        HealthComponent hc = (HealthComponent)Game.getHero().get().getComponent(HealthComponent.class).get();
        hc.setCurrentHealthpoints(hc.getMaximalHealthpoints());
    }

    /**
     * Get the Quest condition as String
     * **/
    @Override
    public String getConditionToString() {
        return ((Hero)Game.getHero().get()).getMoney()+"/"+condition;
    }
}
