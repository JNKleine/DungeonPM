package ecs.Quests;
import ecs.Quests.QuestBehavior.QuestBehavior;
import ecs.entities.Entity;

import java.util.logging.Logger;

/**
 * The quest is for holding all the information about a specific quest
 * **/
public class Quest {
    private final Logger questLogger;
    private String shortDescription;
    private String questDescription;

    private String questShortDescriptionWithProgress;
    private QuestBehavior questCondition;

    private String questIsFinishedQuestText;
    private boolean questIsFinished;

    private QuestTag questTag;

    private boolean alreadyChecked;

    /**
     * Construct a Quest
     * <p>A Quest needs descriptions, an Behavior and an unique QuestTag.
     * The QuestTag is for this specific Quest and for all their objectives.
     * E.g. if an Entity is a QuestObjective, this Entity should get the QuestObjectiveComponent and the
     * QuestTag from this Quest</p>
     *
     * @param questShortDescription: short description for this quest
     * @param questDescription: description, a Quest giver should say
     * @param questCondition: The Condition, that the Quest should have (QuestBehavior)
     * @param questTag: The unique QuestTag for the Quest
     * @param finishedText: The text, that appears if the quest is finished
     * **/
    public Quest(String questShortDescription,String questDescription, QuestBehavior questCondition, QuestTag questTag,String finishedText) {
        this.shortDescription = questShortDescription;
        this.questDescription = questDescription;
        this.questCondition = questCondition;
        this.questTag = questTag;
        this.questIsFinishedQuestText = finishedText;
        this.questIsFinished = false;
        this.alreadyChecked= false;
        questLogger = Logger.getLogger(this.getClass().getName());

        this.questShortDescriptionWithProgress = shortDescription+" "+questCondition.getConditionToString();
    }

    /**
     * Check the condition of completing the quest
     * **/
    public void checkCondition() {
        questIsFinished = questCondition.checkQuestCondition();
        if(questIsFinished && !alreadyChecked) {
            alreadyChecked = true;
            questLogger.info("Quest "+this.getShortQuestDescriptionWithProgress()+" is finished");
            this.questShortDescriptionWithProgress = shortDescription+"("+questIsFinishedQuestText+")";
        } else if(!questIsFinished) {
            this.questShortDescriptionWithProgress = shortDescription + " " + questCondition.getConditionToString();
        }
        }

    /**
     * Get a boolean that says whether this quest has been completed
     * @return boolean true if quest is finished, otherwise false
     * **/
    public boolean isQuestIsFinished() {
        return questIsFinished;
    }

    /**
     * Increase Progress from this quest
     * **/
    public void increaseProgressForQuest() {
        questCondition.increaseProgress();
    }

    /**
     * Get the Reward for finishing the quest
     * @param e: Entity that should be rewarded
     * **/
    public void getReward(Entity e) {
        questLogger.info("Quest "+this.getShortQuestDescriptionWithProgress()+" reward requested");
        questCondition.getReward(e);
    }

    /**
     * Get the QuestTag
     * @return QuestTag from this Quest
     * **/
    public QuestTag getQuestTag() {
        return questTag;
    }

    /**
     * Get the Quest description
     * @return Description of this quest
     * **/
    public String getQuestDescription() {
        return questDescription;
    }

    /**
     * Get the short description from this Quest
     * @return String with short description
     * **/
    public String getShortQuestDescriptionWithProgress() {
        return questShortDescriptionWithProgress;

    }
}
