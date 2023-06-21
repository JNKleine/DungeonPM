package ecs.systems;

import ecs.Quests.Quest;
import ecs.components.QuestLogComponent;
import ecs.entities.Entity;
import starter.Game;

/** The quest system is for checking if a quest has been completed * */
public class QuestSystem extends ECS_System {
    private Entity hero;
    private QuestLogComponent qlC;

    /** Creates the QuestSystem * */
    public QuestSystem() {
        hero = Game.getHero().get();
        qlC = (QuestLogComponent) hero.getComponent(QuestLogComponent.class).get();
    }

    /** Determines what happens each frame * */
    @Override
    public void update() {
        if (qlC != null && !qlC.isEmpty()) {
            qlC.checkQuestConditions();
        }
        QuestLogComponent qlC =
                (QuestLogComponent)
                        Game.getHero().get().getComponent(QuestLogComponent.class).get();
        Quest clickedQuest = Game.questHUD.quest;
        if (Game.questHUD.buttonIsClicked
                && clickedQuest != null
                && clickedQuest.isQuestIsFinished()) {
            clickedQuest.getReward(Game.getHero().get());
            qlC.removeQuest(clickedQuest);
            if (qlC.getMainQuest() != null && qlC.getMainQuest().equals(clickedQuest))
                qlC.setCurMainQuest(null);
            Game.questHUD.buttonIsClicked = false;
            Game.questHUD.quest = null;
            Game.callQuestHUD();
            Game.callQuestHUD();
        }
    }
}
