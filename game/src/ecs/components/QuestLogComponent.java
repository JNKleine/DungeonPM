package ecs.components;

import ecs.Quests.Quest;
import ecs.Quests.QuestTag;
import ecs.entities.Entity;
import java.util.ArrayList;
import java.util.logging.Logger;

/**
 * This component is used to give an entity (especially the player) a quest log in which active
 * quests are managed *
 */
public class QuestLogComponent extends Component {

    private final Logger questComponentLogger;

    private ArrayList<Quest> questLog;
    private int questLogSize;

    private Quest mainQuest;
    /**
     * Create a new component and add it to the associated entity
     *
     * @param entity associated entity
     */
    public QuestLogComponent(Entity entity, int questLogSize) {
        super(entity);
        questComponentLogger = Logger.getLogger(this.getClass().getName());
        questLog = new ArrayList<>();
        this.questLogSize = questLogSize;
    }

    /**
     * Add a quest to the log if the log is not full
     *
     * @param quest: The quest to be added
     * @return boolean, which says whether the quest could be inserted *
     */
    public boolean addQuestToLog(Quest quest) {
        if (questLog.size() < questLogSize) {
            questLog.add(quest);
            questComponentLogger.info(
                    "Added Quest " + quest.getShortQuestDescriptionWithProgress() + " to Log");
            return true;
        }
        return false;
    }

    /**
     * Remove a quest from the log
     *
     * @param quest: Which quest to be removed
     * @return boolean, which says, whether the quest could be removed *
     */
    public boolean removeQuest(Quest quest) {
        questComponentLogger.info(
                "Remove Quest "
                        + quest.getShortQuestDescriptionWithProgress()
                        + " from Log, if existing");
        return questLog.remove(quest);
    }

    /**
     * Get a List with all active quests
     *
     * @return ArrayList from Type Quest *
     */
    public ArrayList<Quest> getQuestList() {
        return questLog;
    }

    /**
     * Determines whether the log is not full
     *
     * @return true if the Log is not full, otherwise false *
     */
    public boolean questLogIsNotFull() {
        return questLog.size() < questLogSize;
    }

    /**
     * Determines whether the log is empty
     *
     * @return true if the Log is empty, otherwise false *
     */
    public boolean isEmpty() {
        return questLog.size() == 0;
    }
    /**
     * Get the maximum possible number of quests in the log
     *
     * @return Size of the Quest log *
     */
    public int getMaxQuestNumber() {
        return questLogSize;
    }

    /** Check quest conditions from all active quests * */
    public void checkQuestConditions() {
        for (Quest q : questLog) {
            q.checkCondition();
        }
    }

    /**
     * Checks if the progress of active quests needs to be refreshed
     *
     * @param questTag : ArrayList with all QuestTags, that need to be checked *
     */
    public void increaseProgress(ArrayList<QuestTag> questTag) {
        for (Quest q : questLog) {
            if (questTag.contains(q.getQuestTag())) {
                questComponentLogger.info(
                        "Update Progress in Quest" + q.getShortQuestDescriptionWithProgress());
                q.increaseProgressForQuest();
            }
        }
    }

    /**
     * Checks if a quest is already in the log
     *
     * @param q : Quest for checking
     * @return boolean, which shows if this quest is already in the log *
     */
    public boolean questIsInLog(Quest q) {
        return questLog.contains(q);
    }

    /**
     * Set current main quest
     *
     * @param quest: Quest for the main quest *
     */
    public void setCurMainQuest(Quest quest) {
        mainQuest = quest;
    }

    /**
     * Get current main quest
     *
     * @return the current main quest *
     */
    public Quest getMainQuest() {
        return mainQuest;
    }

    public void resetToStartValue() {
        questLog = new ArrayList<>();
        mainQuest = null;
    }
}
