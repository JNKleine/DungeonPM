package ecs.Quests;
import ecs.Quests.QuestBehavior.QuestBehavior;
import ecs.entities.Entity;

public class Quest {
    private String shortDescription;
    private String questDescription;

    private String questShortDescriptionWithProgress;
    private QuestBehavior questCondition;

    private String questIsFinishedQuestText;
    private boolean questIsFinished;

    private QuestTag questTag;

    public Quest(String questShortDescription,String questDescription, QuestBehavior questCondition, QuestTag questTag,String finishedText) {
        this.shortDescription = questShortDescription;
        this.questDescription = questDescription;
        this.questCondition = questCondition;
        this.questTag = questTag;
        this.questIsFinishedQuestText = finishedText;
        this.questIsFinished = false;

        this.questShortDescriptionWithProgress = shortDescription+" "+questCondition.getConditionToString();
    }

    public void checkCondition() {
        questIsFinished = questCondition.checkQuestCondition();
        if(questIsFinished) {
            this.questShortDescriptionWithProgress = shortDescription+"("+questIsFinishedQuestText+")";
        } else {
            this.questShortDescriptionWithProgress = shortDescription + " " + questCondition.getConditionToString();
        }
        }

    public boolean isQuestIsFinished() {
        return questIsFinished;
    }

    public void increaseProgressForQuest() {
        questCondition.increaseProgress();
    }

    public void getReward(Entity e) {
        questCondition.getReward(e);
    }

    public QuestTag getQuestTag() {
        return questTag;
    }

    public String getQuestDescription() {
        return questDescription;
    }

    public String getShortQuestDescriptionWithProgress() {
        return questShortDescriptionWithProgress;

    }
}
