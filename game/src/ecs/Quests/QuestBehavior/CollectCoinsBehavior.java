package ecs.Quests.QuestBehavior;

import ecs.components.PositionComponent;
import ecs.entities.Entity;
import ecs.entities.Hero;
import ecs.items.ItemData;
import ecs.items.ItemDataGenerator;
import ecs.items.WorldItemBuilder;
import starter.Game;

public class CollectCoinsBehavior implements QuestBehavior {
    private int condition;

    /**
     * Construct the collect coins behavior
     *
     * @param condition the amount of money that has to be collected for fulfilling the quest
     */
    public CollectCoinsBehavior(int condition) {
        this.condition = condition;
    }

    /** Checks whether the objective of the quest has been fulfilled * */
    @Override
    public boolean checkQuestCondition() {
        return ((Hero) Game.getHero().get()).getMoney() >= condition;
    }

    /** Since this quest is a collecting quest, increaseProgress do nothing here * */
    @Override
    public void increaseProgress() {
        // Nothing to Increase only checkConditions
    }

    /**
     * The entity gets the reward for this quest
     *
     * @param e: Entity, that will be rewarded *
     */
    @Override
    public void getReward(Entity e) {
        ItemDataGenerator ig = new ItemDataGenerator();
        ItemData item = ig.generateQuestLootItemData();
        Entity itemEntity = WorldItemBuilder.buildWorldItem(item);
        PositionComponent pc =
                (PositionComponent)
                        Game.getHero().get().getComponent(PositionComponent.class).get();
        itemEntity.addComponent(new PositionComponent(itemEntity, pc.getPosition()));
    }

    /** Get the Quest condition as String * */
    @Override
    public String getConditionToString() {
        return ((Hero) Game.getHero().get()).getMoney() + "/" + condition;
    }
}
