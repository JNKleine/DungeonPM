package ecs.components;
import ecs.Quests.Quest;
import ecs.Quests.QuestTag;
import ecs.entities.Entity;
import java.util.ArrayList;

public class QuestLogComponent extends Component {

    private ArrayList<Quest> questLog;
    private int questLogSize;

    private Quest mainQuest;
    /**
     * Create a new component and add it to the associated entity
     *
     * @param entity associated entity
     */
    public QuestLogComponent(Entity entity,int questLogSize) {
        super(entity);
        questLog = new ArrayList<>();
       this.questLogSize = questLogSize;
    }

    public boolean addQuestToLog(Quest quest) {
        if(questLog.size() < questLogSize) {
            questLog.add(quest);
            return true;
        }
        return false;
    }

    public boolean removeQuest(Quest quest) {
        return questLog.remove(quest);
    }

    public ArrayList<Quest> getQuestList() {
        return questLog;
    }

    public boolean questLogIsNotFull() {
        return questLog.size() < questLogSize;
    }

    public boolean isEmpty() {
        return questLog.size() == 0;
    }

    public int getMaxQuestNumber() {
        return questLogSize;
    }

    public void checkQuestConditions() {
        for(Quest q: questLog) {
                q.checkCondition();
        }
    }

    public void increaseProgress(ArrayList<QuestTag> questTag) {
        for(Quest q: questLog) {
            if(questTag.contains(q.getQuestTag())) {
                q.increaseProgressForQuest();
            }
        }
    }

    public void setCurMainQuest(Quest quest) {
        mainQuest = quest;
    }

    public Quest getMainQuest() {
        return mainQuest;
    }
}
