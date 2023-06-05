package ecs.systems;

import ecs.Quests.Quest;
import ecs.components.QuestLogComponent;
import ecs.entities.Entity;
import starter.Game;

public class QuestSystem extends ECS_System{
    private Entity hero;
    private QuestLogComponent qlC;



    public QuestSystem() {
        hero = Game.getHero().get();
        qlC = (QuestLogComponent) hero.getComponent(QuestLogComponent.class).get();
    }

    @Override
    public void update() {
        if(qlC != null && !qlC.isEmpty()) {
            qlC.checkQuestConditions();
        }
       QuestLogComponent qlC = (QuestLogComponent)Game.getHero().get().getComponent(QuestLogComponent.class).get();
        Quest clickedQuest = Game.questHUD.quest;
        if(Game.questHUD.buttonIsClicked && clickedQuest != null && clickedQuest.isQuestIsFinished()) {
            clickedQuest.getReward(Game.getHero().get());
            qlC.removeQuest(clickedQuest);
            if(qlC.getMainQuest() != null && qlC.getMainQuest().equals(clickedQuest)) qlC.setCurMainQuest(null);
            Game.questHUD.buttonIsClicked = false;
            Game.questHUD.quest = null;
            Game.callQuestHUD();
            Game.callQuestHUD();
        }
    }
}
