package ecs.components;

import ecs.Quests.QuestTag;
import ecs.entities.Entity;

import java.util.ArrayList;

public class QuestObjectiveComponent extends Component {

    private ArrayList<QuestTag> partOfQuests;
    /**
     * Create a new component and add it to the associated entity
     *
     * @param entity associated entity
     */
    public QuestObjectiveComponent(Entity entity,ArrayList<QuestTag> partOfQuests) {
        super(entity);
        this.partOfQuests = partOfQuests;
    }

    public QuestObjectiveComponent(Entity entity,QuestTag questTag) {
        super(entity);
        partOfQuests = new ArrayList<>();
        this.partOfQuests.add(questTag);
    }

    public ArrayList<QuestTag> getPartOfQuests() {
        return partOfQuests;
    }

    public void addQuestTag(QuestTag questTag) {
        partOfQuests.add(questTag);
    }

    public void addQuestTagList(ArrayList<QuestTag> questTagList) {
        for(QuestTag q: questTagList) {
            addQuestTag(q);
        }
    }

    public void changeQuestTagList(ArrayList<QuestTag> questTagList) {
        partOfQuests = questTagList;
    }
}
