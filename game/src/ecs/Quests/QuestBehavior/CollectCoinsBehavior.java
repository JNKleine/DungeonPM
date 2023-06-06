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
    @Override
    public boolean checkQuestCondition() {
        return ((Hero)Game.getHero().get()).getMoney() >= condition;
    }

    @Override
    public void increaseProgress() {
      //Nothing to Increase only checkConditions
    }

    @Override
    public void getReward(Entity e) {
        HealthComponent hc = (HealthComponent)Game.getHero().get().getComponent(HealthComponent.class).get();
        hc.setCurrentHealthpoints(hc.getMaximalHealthpoints());
    }

    @Override
    public String getConditionToString() {
        return ((Hero)Game.getHero().get()).getMoney()+"/"+condition;
    }
}
